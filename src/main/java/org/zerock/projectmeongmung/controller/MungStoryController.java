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

import java.util.Collections;

@Controller
@RequestMapping("/mungstory")
@RequiredArgsConstructor
@Log4j2
public class MungStoryController {

    private final MeongStoryService service;

    // 기본적으로 초기 데이터를 로드하여 전달
    @GetMapping
    public String meongStory(Model model, PageRequestDTO pageRequestDTO) {
        PageResultDTO<MeongStoryDTO, MeongStory> result = service.getAllItems(pageRequestDTO);
        PageResultDTO<MeongStoryDTO, MeongStory> resultPaged = service.getList(pageRequestDTO);

        model.addAttribute("result", result);
        model.addAttribute("resultPaged", resultPaged);
        model.addAttribute("current", "1"); // 기본적으로 '1'을 설정
        return "mungStoryHtml/storyboard";
    }

    // 수정된 메서드 (dataList를 사용하지 않음)
    @GetMapping("/mungstoryAll")
    public String mungstoryAll(@RequestParam String current, Model model, PageRequestDTO pageRequestDTO) {
        log.info("list........." + pageRequestDTO);

        PageResultDTO<MeongStoryDTO, MeongStory> result;

        switch (current) {
            case "1":
                result = service.getList(pageRequestDTO);
                break;
            case "2":
                result = service.getPetFriendlyLocations(pageRequestDTO);
                break;
            case "3":
                result = service.getDailyItems(pageRequestDTO);
                break;
            default:
                // Empty Page with a transformation function
                result = new PageResultDTO<>(Page.empty(pageRequestDTO.getPageable()), this::entityToDto);
                break;
        }

        model.addAttribute("result", result);

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

/*    @PostMapping("/modify") //위에 modify는 get방식으로 화면을 띄울때만 사용 post방식을 이용하여 데이터를 넘기겠다
    // RedirectAttributes redirectAttributes 뷰 페이지에 전달, 일회성
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
    }*/
}
