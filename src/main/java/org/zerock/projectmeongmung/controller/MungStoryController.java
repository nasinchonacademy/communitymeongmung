package org.zerock.projectmeongmung.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.projectmeongmung.dto.*;
import org.zerock.projectmeongmung.entity.MeongStory;
import org.zerock.projectmeongmung.entity.Notice;
import org.zerock.projectmeongmung.entity.StoryComment;
import org.zerock.projectmeongmung.entity.User;
import org.zerock.projectmeongmung.repository.MeongStoryRepository;
import org.zerock.projectmeongmung.repository.NoticeRepository;
import org.zerock.projectmeongmung.repository.StoryCommentRepository;
import org.zerock.projectmeongmung.repository.UserRepository;
import org.zerock.projectmeongmung.service.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/mungstory")
@RequiredArgsConstructor
@Log4j2
public class MungStoryController {


    @Autowired
    private UserDetailService userDetailService;

    private final MeongStoryService service;
    private final MeongStoryCommentService serviceC;
    private final MeongStoryRepository meongStoryRepository;
    private final StoryLikeService storyLikeService;
    private final UserRepository userRepository;
    private final StoryCommentRepository storyCommentRepository;
    private final NoticeRepository noticeRepository;



    // 기본적으로 초기 데이터를 로드하여 전달
    @GetMapping
    public String meongStory(@RequestParam(value = "page", defaultValue = "1") int page,
                             @RequestParam(value = "size", defaultValue = "10") int size,
                             @RequestParam(value = "type", required = false) String type,
                             @RequestParam(value = "keyword", required = false) String keyword,
                             @RequestParam(value = "current", defaultValue = "1") int current,
                             PageRequestDTO pageRequestDTO, Model model,
                             Authentication authentication) {

        // 로그로 파라미터 값을 확인
        log.info("Received page: {}, size: {}, type: {}, keyword: {}, current: {}", page, size, type, keyword, current);

        // pageRequestDTO에 페이지 정보, 크기, 타입, 키워드를 설정
        pageRequestDTO.setPage(page);
        pageRequestDTO.setSize(size);
        pageRequestDTO.setType(type);
        pageRequestDTO.setKeyword(keyword);

        PageResultDTO<MeongStoryDTO, MeongStory> result;

        // current 값에 따라 다른 서비스 메서드를 호출
        switch (current) {
            case 1:
                result = service.getAllItems(pageRequestDTO); // 검색어를 활용한 검색 로직 포함
                break;
            case 2:
                result = service.getPetFriendlyLocations(pageRequestDTO); // 검색어를 활용한 검색 로직 포함
                break;
            case 3:
                result = service.getDailyItems(pageRequestDTO); // 검색어를 활용한 검색 로직 포함
                break;
            default:
                result = new PageResultDTO<>(Page.empty(pageRequestDTO.getPageable()), this::entityToDto);
                break;
        }

        // 로그로 결과 확인
        log.info("Result: {}", result);

        result.getDtoList().forEach(dto -> dto.setTitle(service.trimTitle(dto.getTitle())));

        // 모델에 결과 및 현재 페이지, 검색어를 설정
        model.addAttribute("result", result);
        model.addAttribute("current", current);
        model.addAttribute("pageRequestDTO", pageRequestDTO);

        return "mungStoryHtml/storyboard";
    }

    // 추가된 메서드: Entity를 DTO로 변환하는 메서드
    private MeongStoryDTO entityToDto(MeongStory entity) {
        return MeongStoryDTO.builder()
                .seq(entity.getSeq())
                .title(entity.getTitle())
                .content(entity.getContent())
                .likecount(entity.getLikecount())
                .picture(entity.getPicture())
                .viewcount(entity.getViewcount())
                .category(entity.getCategory())
                .regdate(entity.getRegdate())
                .modified(entity.getModified())
                .deleted(entity.getDeleted())
                .uid(entity.getUser().getUid())
                .nickname(entity.getUser().getNickname())
                .build();
    }

    @GetMapping("/storywirte")
    public String storywirte(Model model,Authentication authentication) {
        String username = authentication.getName();
        User user = userDetailService.findUserByUid(username);
        model.addAttribute("user", user);
        return "mungStoryHtml/storywirte";
    }

    @PostMapping("/storywirte")
    public String storywirtePost(MeongStoryDTO dto, RedirectAttributes redirectAttributes,
                                 @RequestParam("file") MultipartFile file,
                                 Model model) {

        log.info("dto..." + dto);

        // 파일이 비어 있지 않은지 확인
        if (!file.isEmpty()) {
            try {
                // 파일 저장 메서드를 호출하여 파일을 저장하고, 저장된 파일명을 DTO에 설정
                String savedFileName = saveBoardPhoto(file);
                dto.setPicture(savedFileName); // 파일명을 DTO의 picture 필드에 설정

            } catch (IOException e) {
                e.printStackTrace();
                model.addAttribute("message", "파일 업로드 실패");
                return "error"; // 오류 페이지로 리다이렉트 또는 표시
            }
        }

        // 서비스 계층을 통해 게시글 등록
        service.register(dto);

        return "redirect:/mungstory";
    }

    public String saveBoardPhoto(MultipartFile file) throws IOException {
        // 저장할 경로 설정
        String uploadDir = "C:\\work\\uploads\\";

        // 파일명을 고유하게 하기 위해 UUID를 사용
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

        // 전체 경로 생성
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String filePath = uploadDir + fileName;

        // 파일을 지정된 경로에 저장
        file.transferTo(new java.io.File(filePath));

        // 저장된 파일의 경로를 반환
        return fileName;
    }

    @GetMapping("/storyread")
    public String storylistread(@RequestParam("seq") long seq,
                                @ModelAttribute("requestDTO") PageRequestDTO requestDTO,
                                Model model,
                                @RequestParam("current") int current,
                                RedirectAttributes redirectAttributes) {

        // 조회수 증가
        service.incrementViewCount(seq);

        MeongStoryDTO dto = service.read(seq);

        // 현재 로그인한 사용자 닉네임을 model에 추가
        User user = (User) model.getAttribute("user");
        if (user != null) {
            model.addAttribute("userNickname", user.getNickname());
        } else {
            model.addAttribute("userNickname", "Guest");
        }

        model.addAttribute("dto", dto);
        model.addAttribute("current", current);

        // 댓글 목록 로드
        List<StoryCommentDto> commentDtoList = serviceC.getCommentsByStorySeq(seq);
        if (commentDtoList == null || commentDtoList.isEmpty()) {
            model.addAttribute("error", "No comments found for this story");
        } else {
            model.addAttribute("commentDtoList", commentDtoList);
        }

        return "mungStoryHtml/storyread";
    }



    @GetMapping("/storyedit") //위에 modify는 get방식으로 화면을 띄울때만 사용 post방식을 이용하여 데이터를 넘기겠다
    public String storyedit(long seq,
                            @ModelAttribute("requestDTO") PageRequestDTO requestDTO, Model model,
                            @RequestParam("current") int current
    ){

        log.info(seq);

        MeongStoryDTO dto= service.read(seq);

        model.addAttribute("dto", dto);
        model.addAttribute("current", current); // 현재 선택된 라디오 버튼 값 추가

        return "mungStoryHtml/storyedit";
    }

    @PostMapping("/storyedit") //위에 modify는 get방식으로 화면을 띄울때만 사용 post방식을 이용하여 데이터를 넘기겠다
    // RedirectAttributes redirectAttributes 뷰 페이지에 전달, 일회성
    public String modify(MeongStoryDTO dto , @ModelAttribute("requestDTO") PageRequestDTO requestDTO,
                         Authentication authentication,
                         Model model,
                         RedirectAttributes redirectAttributes,
                         @RequestParam("current") int current,
                         @RequestParam(value = "file", required = false) MultipartFile file){


        String username = authentication.getName();
        User user = userDetailService.findUserByUid(username);
        model.addAttribute("user", user);

        log.info("post modify...");
        log.info("dto: " + dto);

        // 파일 처리 로직
        if (file != null && !file.isEmpty()) {
            try {
                // FileController의 파일 저장 메서드 사용
                FileController fileController = new FileController();
                String savedFileName = fileController.saveProfilePhoto(file);
                dto.setPicture(savedFileName);  // 파일명을 DTO에 설정 (DB에 저장할 수 있도록)
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        service.modify(dto);

        redirectAttributes.addAttribute("page",requestDTO.getPage());
        redirectAttributes.addAttribute("type",requestDTO.getType());
        redirectAttributes.addAttribute("keyword",requestDTO.getKeyword());
        redirectAttributes.addAttribute("current", current); // 현재 선택된 라디오 버튼 값 추가
        redirectAttributes.addAttribute("seq",dto.getSeq());

        return "redirect:/mungstory/storyread";
    }

    @PostMapping("/like")
    public ResponseEntity<?> like(@RequestBody LikeRequest request, Authentication authentication) {
        // 인증된 사용자의 UID를 가져옴
        String uid = authentication.getName();  // 여기서 인증된 사용자의 UID를 가져옴

        // UID를 통해 사용자 정보를 가져옴
        User currentUser = userDetailService.findUserByUid(uid);

        // 게시물 ID로 MeongStory 객체를 조회
        MeongStory story = meongStoryRepository.findById(request.getSeq()).orElse(null);

        if (story == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("게시물을 찾을 수 없습니다.");
        }

        LocalDate today = LocalDate.now();

        // 데이터베이스에서 사용자 좋아요 기록 조회
        boolean hasLikedToday = storyLikeService.hasLikedBefore(currentUser, story);

        if (hasLikedToday) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("하루에 한 번만 좋아요를 누를 수 있습니다.");
        }

        // 좋아요 처리 로직
        storyLikeService.saveLikeRecord(currentUser, story, today);
        storyLikeService.incrementLikeCount(story);

        // 최신 likecount 값을 포함한 응답 생성
        return ResponseEntity.ok(Map.of("likecount", story.getLikecount() + 1)); // story.getLikeCount()는 증가된 좋아요 수를 반환합니다.
    }


    @PostMapping("/remove")
    public String remove(long seq,
                         RedirectAttributes redirectAttributes,
                         Model model,
                         @RequestParam("current") int current){
        log.info("seq: " + seq);

        service.remove(seq);

        redirectAttributes.addFlashAttribute("msg", seq);
        model.addAttribute("current", current); // 현재 선택된 라디오 버튼 값 추가

        return "redirect:/mungstory";
    }

    // 댓글을 읽어오는 메서드
    @GetMapping("/read")
    public String commentonlyread(Model model,
                                  @RequestParam("seq") Long seq,
                                  RedirectAttributes redirectAttributes) {
        log.info("Reading comment for story seq: " + seq);

        return "fragmants/mungStory/mungCommentContent"; // 이 경로에 맞는 템플릿이 있는지 확인하세요.
    }

    @GetMapping("/comments")
    public ResponseEntity<List<Map<String, Object>>> getComments(@RequestParam("seq") Long seq) {
        List<StoryComment> comments = storyCommentRepository.findByStorySeq(seq);

        // 날짜 포맷터 생성 (MM/dd/HH/mm/ss 형식)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd HH:mm:ss");

        List<Map<String, Object>> commentDataList = comments.stream().map(comment -> {
            Map<String, Object> commentData = new HashMap<>();
            commentData.put("commentId", comment.getCommentid());  // 댓글 ID 추가
            commentData.put("seq", seq);  // 게시글 ID 추가
            commentData.put("nickname", comment.getUser().getNickname());
            commentData.put("profilePhoto", comment.getUser().getProfilePhoto()); // 프로필 사진 추가
            commentData.put("commentcontent", comment.getCommentcontent());

            // regdate와 modified 날짜를 MM/dd/HH/mm/ss 형식으로 포맷
            String regdate = comment.getRegdate().format(formatter);  // 등록일 포맷
            String modified = comment.getModified() != null
                    ? comment.getModified().format(formatter)
                    : "수정되지 않음";  // 수정일 포맷 (null 처리)

            commentData.put("regdate", regdate);  // 포맷된 등록일
            commentData.put("modified", modified);  // 포맷된 수정일

            return commentData;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(commentDataList);
    }

    @PostMapping("/addcomment")
    public ResponseEntity<Map<String, Object>> addComment(@RequestParam("seq") Long seq,
                                                          @RequestParam("commentcontent") String commentContent,
                                                          @RequestParam("userId") Long userId,
                                                          @RequestParam("uid") String uid,
                                                          @RequestParam("title") String title,
                                                          RedirectAttributes redirectAttributes) {

        log.info("Adding comment to story seq: " + seq);

        serviceC.incrementCommentCount(seq);

        MeongStory story = meongStoryRepository.findById(seq).orElse(null);

        if (story == null) {
            log.error("Story not found with seq: " + seq);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Story not found"));
        }



        // 댓글 생성 및 저장
        StoryComment storyComment = StoryComment.builder()
                .commentcontent(commentContent)
                .user(userRepository.findById(userId)
                        .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId)))
                .story(story)
                .build();

        storyCommentRepository.save(storyComment); // 댓글 저장


        // User 객체 가져오기 (User는 Notice와 연관됨)
        User user = userRepository.findByUid(uid)
                .orElseThrow(() -> new RuntimeException("User not found"));


//        String message = "[" + title + "] 게시글에<br> " + "새로운 댓글이 달렸습니다.";



        String message = "<a href='/mungstory/storyread?page=1&type=&keyword=&current=1&seq=" + seq + "'>"
                + "[" + title + "]" +"<br>게시글에 새로운 댓글이 달렸습니다.</a>";
        Notice notice = new Notice(message, user);
        noticeRepository.save(notice);  // Notice 테이블에 저장, user_id 참조
        redirectAttributes.addFlashAttribute("message", message);

        storyCommentRepository.save(storyComment); // 댓글 저장

        // 날짜 포맷팅 설정
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd HH:mm:ss");





        // 댓글 저장 후 응답으로 날짜 정보 포함
        return ResponseEntity.ok(Map.of(
                "commentcount", story.getCommentcount(),
                "nickname", storyComment.getUser().getNickname(),
                "regdate", storyComment.getRegdate().format(formatter),  // 포맷팅된 등록일
                "modified", storyComment.getModified() != null ? storyComment.getModified().format(formatter) : "수정되지 않음"  // 포맷팅된 수정일
        ));
    }





    @PostMapping("/commentremove")
    public ResponseEntity<Map<String, Object>> removeComment(
            @RequestParam("commentId") Long commentId,
            @RequestParam("seq") Long seq) {

        log.info("Deleting comment with ID: " + commentId + " for post seq: " + seq);

        try {
            // 댓글 삭제 로직 호출
            serviceC.removeComment(commentId);
            serviceC.decrementCommentCount(seq);

            // 성공적으로 처리되었음을 응답
            Map<String, Object> response = new HashMap<>();
            response.put("message", "댓글이 삭제되었습니다.");
            response.put("status", "success");

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("댓글 삭제 실패", e);

            // 에러 발생 시 처리
            Map<String, Object> response = new HashMap<>();
            response.put("message", "댓글 삭제 중 문제가 발생했습니다.");
            response.put("status", "error");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
}
