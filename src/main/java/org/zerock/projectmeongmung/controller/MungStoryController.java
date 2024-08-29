package org.zerock.projectmeongmung.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.projectmeongmung.dto.MeongStoryDTO;
import org.zerock.projectmeongmung.dto.PageRequestDTO;
import org.zerock.projectmeongmung.dto.PageResultDTO;
import org.zerock.projectmeongmung.entity.MeongStory;
import org.zerock.projectmeongmung.service.MeongStoryService;

@Controller
@RequestMapping("/mungstory")
@RequiredArgsConstructor
@Log4j2
public class MungStoryController {

    private final MeongStoryService service;

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

    // 글 상세보기
    @GetMapping("/storylist")
    public void storylist(long seq, @ModelAttribute("requestDTO") PageRequestDTO requestDTO, Model model) {
        log.info(seq);
        MeongStoryDTO dto = service.read(seq);
        model.addAttribute("dto", dto);
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

    @PostMapping("/modify") //위에 modify는 get방식으로 화면을 띄울때만 사용 post방식을 이용하여 데이터를 넘기겠다
    public String modify(MeongStoryDTO dto , @ModelAttribute("requestDTO") PageRequestDTO requestDTO,
                         RedirectAttributes redirectAttributes){

        log.info("post modify...");
        log.info("dto: " + dto);

        service.modify(dto);

        redirectAttributes.addAttribute("page",requestDTO.getPage());
        redirectAttributes.addAttribute("type",requestDTO.getType());
        redirectAttributes.addAttribute("keyword",requestDTO.getKeyword());

        redirectAttributes.addAttribute("seq",dto.getSeq());

        return "redirect:/guestbook/read";
    }
}
