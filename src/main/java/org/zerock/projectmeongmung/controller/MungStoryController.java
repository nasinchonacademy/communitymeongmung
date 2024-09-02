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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.projectmeongmung.dto.LikeRequest;
import org.zerock.projectmeongmung.dto.MeongStoryDTO;
import org.zerock.projectmeongmung.dto.PageRequestDTO;
import org.zerock.projectmeongmung.dto.PageResultDTO;
import org.zerock.projectmeongmung.entity.MeongStory;
import org.zerock.projectmeongmung.entity.User;
import org.zerock.projectmeongmung.repository.MeongStoryRepository;
import org.zerock.projectmeongmung.service.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/mungstory")
@RequiredArgsConstructor
@Log4j2
public class MungStoryController {


    @Autowired
    private UserDetailService userDetailService;

    private final MeongStoryService service;
    private final MeongStoryService meongStoryService;
    private final MeongStoryRepository meongStoryRepository;
    private final StoryLikeService storyLikeService;


    // 기본적으로 초기 데이터를 로드하여 전달
    @GetMapping
    public String meongStory(@RequestParam(value = "page", defaultValue = "1") int page,
                             @RequestParam(value = "size", defaultValue = "10") int size,
                             @RequestParam(value = "type", required = false) String type,
                             @RequestParam(value = "keyword", required = false) String keyword,
                             @RequestParam(value = "current", defaultValue = "1") int current,
                             PageRequestDTO pageRequestDTO, Model model) {

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

        // 모델에 결과 및 현재 페이지, 검색어를 설정
        model.addAttribute("result", result);
        model.addAttribute("current", current);
        model.addAttribute("pageRequestDTO", pageRequestDTO);


        return "mungStoryHtml/storyboard";
    }


    // AJAX 요청을 처리하는 메서드
    @GetMapping("/mungstoryAll")
    public String mungstoryAll(@RequestParam("current") int current,
                               @RequestParam("page") int page,
                               PageRequestDTO pageRequestDTO, Model model) {

        pageRequestDTO.setPage(page);

        PageResultDTO<MeongStoryDTO, MeongStory> result;

        switch (current) {
            case 1:
                result = service.getList(pageRequestDTO);
                break;
            case 2:
                result = service.getPetFriendlyLocations(pageRequestDTO);
                break;
            case 3:
                result = service.getDailyItems(pageRequestDTO);
                break;
            default:
                result = new PageResultDTO<>(Page.empty(pageRequestDTO.getPageable()), this::entityToDto);
                break;
        }

        model.addAttribute("result", result);
        model.addAttribute("current", current); // 현재 선택된 라디오 버튼 값 추가

        return "fragments/mungStory/mainContent :: content";
    }

/*    // 글 상세보기
    @GetMapping("/storylist")
    public void storylist(long seq, @ModelAttribute("requestDTO") PageRequestDTO requestDTO, Model model) {
        log.info(seq);
        MeongStoryDTO dto = service.read(seq);
        model.addAttribute("dto", dto);
    }*/

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
        User user = userDetailService.loadUserByUsername(username);
        model.addAttribute("user", user);
        return "mungStoryHtml/storywirte";
    }

    @PostMapping("/storywirte")
    public String storywirtePost(MeongStoryDTO dto, RedirectAttributes redirectAttributes){

        log.info("dto..." + dto);
        Long gno = service.register(dto);

        return "redirect:/mungstory";
    }

    @GetMapping("/storyread")
    public String storylistread(long seq,
                                @ModelAttribute("requestDTO") PageRequestDTO requestDTO, Model model,
                                @RequestParam("current") int current
){

        log.info(seq);

        MeongStoryDTO dto= service.read(seq);

        model.addAttribute("dto", dto);
        model.addAttribute("current", current); // 현재 선택된 라디오 버튼 값 추가

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
                         @RequestParam("current") int current){


        String username = authentication.getName();
        User user = userDetailService.loadUserByUsername(username);
        model.addAttribute("user", user);

        log.info("post modify...");
        log.info("dto: " + dto);

        service.modify(dto);

        redirectAttributes.addAttribute("page",requestDTO.getPage());
        redirectAttributes.addAttribute("type",requestDTO.getType());
        redirectAttributes.addAttribute("keyword",requestDTO.getKeyword());
        redirectAttributes.addAttribute("current", current); // 현재 선택된 라디오 버튼 값 추가
        redirectAttributes.addAttribute("seq",dto.getSeq());

        return "redirect:/mungstory/storyread";
    }

    @PostMapping("/like")
    public ResponseEntity<?> like(@RequestBody LikeRequest request) {
        User currentUser = getCurrentUser(); // 현재 로그인한 사용자의 User 객체를 가져오는 메소드

        // 게시물 ID로 MeongStory 객체를 조회
        MeongStory story = meongStoryRepository.findById(request.getSeq()).orElse(null);

        if (story == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("게시물을 찾을 수 없습니다.");
        }

        LocalDate today = LocalDate.now();

        // 데이터베이스에서 사용자 좋아요 기록 조회
        boolean hasLikedToday = storyLikeService.hasLikedToday(currentUser, story, today);

        if (hasLikedToday) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("하루에 한 번만 좋아요를 누를 수 있습니다.");
        }

        // 좋아요 처리 로직
        storyLikeService.saveLikeRecord(currentUser, story, today);
        storyLikeService.incrementLikeCount(story);

        // 최신 likecount 값을 포함한 응답 생성
        return ResponseEntity.ok(Map.of("likecount", story.getLikecount() + 1)); // story.getLikeCount()는 증가된 좋아요 수를 반환합니다.
    }


    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            return (User) authentication.getPrincipal();
        }
        throw new RuntimeException("사용자 인증 정보가 없습니다.");
    }



    @PostMapping("/remove")
    public String remove(long seq, RedirectAttributes redirectAttributes,
                         Model model,
                         @RequestParam("current") int current){
        log.info("seq: " + seq);

        service.remove(seq);

        redirectAttributes.addFlashAttribute("msg", seq);
        model.addAttribute("current", current); // 현재 선택된 라디오 버튼 값 추가

        return "redirect:/mungstory";
    }
}
