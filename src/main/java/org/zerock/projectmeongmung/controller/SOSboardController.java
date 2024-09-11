package org.zerock.projectmeongmung.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.projectmeongmung.dto.LikeRequest;
import org.zerock.projectmeongmung.dto.PageRequestDTO;
import org.zerock.projectmeongmung.dto.SOSBoardCommentDto;
import org.zerock.projectmeongmung.dto.SOSboardDTO;
import org.zerock.projectmeongmung.entity.*;
import org.zerock.projectmeongmung.repository.SOSBoardCommentRepository;
import org.zerock.projectmeongmung.repository.SOSboardRepository;
import org.zerock.projectmeongmung.repository.StoryCommentRepository;
import org.zerock.projectmeongmung.repository.UserRepository;
import org.zerock.projectmeongmung.service.*;

import java.io.IOException;
import java.security.Security;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequiredArgsConstructor
@Log4j2
public class SOSboardController {

    @Autowired
    private UserDetailService userDetailService;

    private final SOSboardService sosboardService;
    private final UserRepository userRepository;
    private final FileController fileController;
    private final SOSBoardCommentService commentService;
    private final SOSboardRepository sosboardrepository;
    private final SOSlilkeService slilkeService;
    private final SOSBoardCommentRepository sosboardCommentRepository;
    private final VetController vetController;
    private final VetService vetService;

    @GetMapping("/soshospitallist")
    public String listContent(
            @RequestParam(value = "current", defaultValue = "radio1") String current,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "keyword", required = false) String keyword,
            Model model
    ) {
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(page)
                .size(size)
                .keyword(keyword)
                .build();

        // 디버깅을 위해 키워드 로그 출력
        System.out.println("Keyword: " + keyword);

        // 1페이지일 때만 좋아요가 높은 3개의 게시물을 가져옵니다.
        if (page == 1) {
            List<SOSboardDTO> top3Boards = sosboardService.getTop3BylikeCount();
            model.addAttribute("top3Boards", top3Boards);  // 좋아요가 높은 게시물
        }

        // 추천수 높은 수의사 정보 추가
        List<Vet> topVets = vetService.getTop3VetsByRecommendation();
        model.addAttribute("vets", topVets);  // 추천수 높은 수의사

        // 라디오 버튼 선택값을 모델에 추가
        model.addAttribute("current", current);
        System.out.println(current);

        // 현재 라디오 선택에 따라 병원 리스트 또는 SOS 게시판 리스트를 처리
        if ("radio1".equals(current)) {
            return "meongsoshtml/soshospitallist";  // 동물병원 리스트 페이지
        } else {
            // SOS 상담 리스트 및 검색 처리
            model.addAttribute("result", sosboardService.searchByKeyword(pageRequestDTO));
            model.addAttribute("pageRequestDTO", pageRequestDTO);

            return "meongsoshtml/soshospitallist2";  // SOS 상담 리스트 페이지
        }
    }

    @GetMapping("/sosboardwrite")
    public String sosBoardWrite(Model model, Authentication authentication,
                                @RequestParam(value = "current", defaultValue = "radio2") String current ){
        String username = authentication.getName();
        User user = userDetailService.findUserByUid(username);
        model.addAttribute("user", user);
        model.addAttribute("current", current);

        return "meongsoshtml/sosboardwrite";
    }

    @PostMapping("/sosboardwrite")
    public String sosBoardWrite(Model model, SOSboardDTO sosboardDTO,
                                @RequestParam("file") MultipartFile file){

        if (!file.isEmpty()) {
            try {
                String filenameFile = fileController.saveProfilePhoto(file);
                sosboardDTO.setPicture(filenameFile);
            } catch (IOException e) {
                e.printStackTrace();
                model.addAttribute("message", "파일 업로드 실패");
                return "error"; // 오류 페이지로 리다이렉트 또는 표시
            }
        }

        sosboardService.saveSOSboard(sosboardDTO);

        return "redirect:/soshospitallist";
    }

    @GetMapping("/sosboardread")
    public String sosboardread(@RequestParam("seq") long seq,
                               @ModelAttribute("requestDTO") PageRequestDTO requestDTO,
                               Model model,@RequestParam("current") String current,
                               RedirectAttributes redirectAttributes) {

        // 조회수 증가
        sosboardService.increaseViewcount(seq);

        SOSboardDTO sosdto = sosboardService.read(seq);

        //현재 로그인한 사용자 닉네임을 model에 추가
        User user =(User)model.getAttribute("user");
        if(user != null){
            model.addAttribute("userNickname", user.getNickname());
        }else {
            model.addAttribute("userNickname", "Guest");
        }
        model.addAttribute("current", current);
        model.addAttribute("sosdto", sosdto);

        //댓글 목록 로드
        List<SOSBoardCommentDto>  commentDtoList = commentService.getCommentsByBoardId(seq);
        if (commentDtoList != null || commentDtoList.isEmpty()) {
            model.addAttribute("error", "No comments found for this story");
        }else{
            model.addAttribute("commentDtoList", commentDtoList);
        }

        return "meongsoshtml/sosboardread";
    }

    @GetMapping("/sosboardedit")
    public String sosboardedit(
            long seq,
            @ModelAttribute("requestDTO") PageRequestDTO requestDTO, Model model,
            @RequestParam("current") String current
    ) {
        log.info(seq);

        SOSboardDTO dto = sosboardService.read(seq);

        model.addAttribute("dto", dto);
        model.addAttribute("current", current);

        return "meongsoshtml/sosqnaedit";
    }

    @PostMapping("/sosboardedit")
    public String modifySosboard(Model model, SOSboardDTO dto,
                                 @ModelAttribute("requestDTO") PageRequestDTO requestDTO,
                                 Authentication authentication,  RedirectAttributes redirectAttributes,
                                 @RequestParam("current") String current){

        String username = authentication.getName();
        User user = userDetailService.findUserByUid(username);
        model.addAttribute("user", user);

        log.info("post modify...");
        log.info("dto: " + dto);

        sosboardService.modify(dto);

        redirectAttributes.addAttribute("page",requestDTO.getPage());
        redirectAttributes.addAttribute("type",requestDTO.getType());
        redirectAttributes.addAttribute("keyword",requestDTO.getKeyword());
        redirectAttributes.addAttribute("current", current); // 현재 선택된 라디오 버튼 값 추가
        redirectAttributes.addAttribute("seq", dto.getSosboardseq());

        return "redirect:/sosboardread";
    }



    @PostMapping("/sosboardlike")
    public ResponseEntity<?> like(@RequestBody LikeRequest request, Authentication authentication) {
        // 인증된 사용자의 UID를 가져옴
        String uid = authentication.getName();  // 여기서 인증된 사용자의 UID를 가져옴

        // UID를 통해 사용자 정보를 가져옴
        User currentUser = userDetailService.findUserByUid(uid);

        // 게시물 ID로 sosboard 객체를 조회
        SOSboard sosboard = sosboardrepository.findById(request.getSeq()).orElse(null);

        if (sosboard == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("게시물을 찾을 수 없습니다.");
        }

        LocalDate today = LocalDate.now();

        // 데이터베이스에서 사용자 좋아요 기록 조회
        boolean hasLikedToday = slilkeService.checkIfUserLiked(currentUser, sosboard, today);

        if (hasLikedToday) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("하루에 한 번만 좋아요를 누를 수 있습니다.");
        }

        // 좋아요 처리 로직
        slilkeService.saveLikeRecord(currentUser, sosboard, today);
        slilkeService.incrementLikeCount(sosboard);

        // 최신 likecount 값을 포함한 응답 생성
        return ResponseEntity.ok(Map.of("likecount", sosboard.getLikecount() + 1)); // story.getLikeCount()는 증가된 좋아요 수를 반환합니다.
    }

    @PostMapping("/sosremove")
    public String remove(long seq,
                         RedirectAttributes redirectAttributes,
                         Model model,
                         @RequestParam("current") String current){
        log.info("seq: " + seq);

        sosboardService.remove(seq);

        redirectAttributes.addFlashAttribute("seq", seq);
        model.addAttribute("current", current); // 현재 선택된 라디오 버튼 값 추가

        return "redirect:/soshospitallist";
    }

    // 댓글을 읽어오는 메서드
    @GetMapping("/sosread")
    public String commentonlyread(Model model,
                                  @RequestParam("seq") Long seq,
                                  RedirectAttributes redirectAttributes) {
        log.info("Reading comment for story seq: " + seq);

        return "fragmants/mungSos/sosboardCommentContent"; // 이 경로에 맞는 템플릿이 있는지 확인하세요.
    }

    @GetMapping("/soscomments")
    public ResponseEntity<List<Map<String, Object>>> getComments(@RequestParam("seq") Long seq) {
        List<SOSboardcomment> comments = sosboardCommentRepository.findBySosboard_Sosboardseq(seq);

        // 날짜 포맷터 생성 (MM/dd/HH/mm/ss 형식)
        SimpleDateFormat formatter = new SimpleDateFormat("MM/dd HH:mm:ss");

        List<Map<String, Object>> commentDataList = comments.stream().map(comment -> {
            Map<String, Object> commentData = new HashMap<>();
            commentData.put("commentId", comment.getCommentid());
            commentData.put("seq", seq);
            commentData.put("nickname", comment.getUser().getNickname());
            commentData.put("isVet", comment.getUser().isVet()); // 수의사 여부 추가
            commentData.put("userid", comment.getUser().getId());
            commentData.put("profilePhoto", comment.getUser().getProfilePhoto()); // 프로필 사진 추가
            commentData.put("commentcontent", comment.getSoscommentcontent());
            commentData.put("likeCount", comment.getLikeCount());  // 좋아요 수 추가

            String regdate = formatter.format(comment.getSoscommentregtime());
            String modified = comment.getSoscommentupdate() != null
                    ? formatter.format(comment.getSoscommentupdate())
                    : "수정되지 않음";

            commentData.put("regdate", regdate);
            commentData.put("modified", modified);



            // 대댓글 정보를 추가
            List<Map<String, Object>> repliesData = comment.getReplies().stream().map(reply -> {
                Map<String, Object> replyData = new HashMap<>();
                User replyUser = userRepository.findById(reply.getUserId())
                        .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));
                replyData.put("replyContent", reply.getReplyContent());  // 대댓글 내용
                replyData.put("replyUserId", reply.getUserId());  // 대댓글 작성자 ID
                replyData.put("replyUserNickname", replyUser.getNickname());  // 대댓글 작성자 닉네임 추가
                replyData.put("replyRegtime", formatter.format(reply.getReplyRegtime()));  // 대댓글 작성 시간
                return replyData;
            }).collect(Collectors.toList());

            commentData.put("replies", repliesData);  // 댓글에 대댓글 리스트 추가

            return commentData;
        }).collect(Collectors.toList());

        return ResponseEntity.ok(commentDataList);
    }



    @PostMapping("/sosaddcomment")
    public ResponseEntity<Map<String, Object>> addComment(@RequestParam("seq") Long seq,
                                                          @RequestParam("commentcontent") String commentContent,
                                                          @RequestParam("userId") Long userId) {

        log.info("Adding comment to story seq: " + seq);

        SOSboard sosboard = sosboardrepository.findById(seq).orElse(null);

        if (sosboard == null) {
            log.error("soSboard not found with seq: " + seq);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Story not found"));
        }

        // 댓글 생성 및 저장
        SOSboardcomment sosboardcomment = SOSboardcomment.builder()
                .soscommentcontent(commentContent)
                .user(userRepository.findById(userId)
                        .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId)))
                .sosboard(sosboard)
                .soscommentregtime(new Date())  // 저장 시 현재 시간으로 설정
                .build();

        sosboardCommentRepository.save(sosboardcomment); // 댓글 저장

        // 날짜 포맷팅 설정
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd HH:mm:ss");

        // Date를 LocalDateTime으로 변환하여 포맷팅
        String regdate = LocalDateTime.ofInstant(sosboardcomment.getSoscommentregtime().toInstant(), ZoneId.systemDefault())
                .format(formatter);

        // 수정일 역시 같은 방식으로 처리
        String modified = sosboardcomment.getSoscommentupdate() != null
                ? LocalDateTime.ofInstant(sosboardcomment.getSoscommentupdate().toInstant(), ZoneId.systemDefault()).format(formatter)
                : "수정되지 않음";

        // 댓글 저장 후 응답으로 날짜 정보 포함
        return ResponseEntity.ok(Map.of(
                "commentcount", sosboard.getCommentcount(),
                "nickname", sosboardcomment.getUser().getNickname(),
                "regdate", regdate,  // 포맷팅된 등록일
                "modified", modified  // 포맷팅된 수정일
        ));
    }

    @PostMapping("/soscommentremove")
    public ResponseEntity<Map<String, Object>> removeComment(
            @RequestParam("commentId") Long commentId,
            @RequestParam("seq") Long seq) {

        log.info("Deleting comment with ID: " + commentId + " for post seq: " + seq);

        try {
            // 댓글 삭제 로직 호출
            commentService.removeComment(commentId);

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

    @PostMapping("/soscommentlike")
    public ResponseEntity<?> likeComment(@RequestBody Map<String, Long> requestData, Authentication authentication) {
        Long commentId = requestData.get("commentId");
        String username = authentication.getName();  // 현재 로그인한 유저 정보

        try {
            int likeCount = commentService.likeComment(commentId, username);
            return ResponseEntity.ok(Map.of("likeCount", likeCount));
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/sosaddreply")
    public ResponseEntity<?> addReply(@RequestBody Map<String, Object> payload) {
        Long commentId = Long.parseLong(payload.get("commentId").toString());
        Long userId = Long.parseLong(payload.get("userId").toString());
        String replyContent = payload.get("replyContent").toString();

        commentService.addReply(commentId, userId, replyContent);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{commentId}/replies")
    public ResponseEntity<List<Reply>> getReplies(@PathVariable Long commentId) {
        List<Reply> replies = commentService.getRepliesByCommentId(commentId);
        return ResponseEntity.ok(replies);
    }


}
