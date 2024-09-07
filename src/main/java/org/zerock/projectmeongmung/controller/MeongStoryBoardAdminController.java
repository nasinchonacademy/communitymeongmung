package org.zerock.projectmeongmung.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.projectmeongmung.dto.MeongStoryDTO;
import org.zerock.projectmeongmung.dto.PageRequestDTO;
import org.zerock.projectmeongmung.dto.PageResultDTO;
import org.zerock.projectmeongmung.dto.StoryCommentDto;
import org.zerock.projectmeongmung.entity.MeongStory;
import org.zerock.projectmeongmung.entity.User;
import org.zerock.projectmeongmung.service.MeongStoryCommentService;
import org.zerock.projectmeongmung.service.MeongStoryService;

import java.util.List;

@Controller
@RequestMapping("/admin")  // 클래스 레벨에서 기본 경로를 설정
@PreAuthorize("hasRole('ADMIN')")  // 관리자 권한이 있는 경우만 접근 가능
@Log4j2
@RequiredArgsConstructor
public class MeongStoryBoardAdminController {

    private final MeongStoryService meongStoryService;
    private final MeongStoryCommentService serviceC;
    private final MeongStoryService service;



    @GetMapping("/getMeongStoryBoardList")
    public String getMeongStoryBoardList(@RequestParam(value = "page", defaultValue = "1") int page,
                                         @RequestParam(value = "size", defaultValue = "10") int size,
                                         @RequestParam(value = "type", required = false) String type,
                                         @RequestParam(value = "keyword", required = false) String keyword,
                                         Model model) {
        // PageRequestDTO 객체 생성
        PageRequestDTO pageRequestDTO = new PageRequestDTO();
        pageRequestDTO.setPage(page);
        pageRequestDTO.setSize(size);
        pageRequestDTO.setType(type);
        pageRequestDTO.setKeyword(keyword);

        // MeongStory 리스트를 가져오는 서비스 호출
        PageResultDTO<MeongStoryDTO, MeongStory> result = meongStoryService.getAllItems(pageRequestDTO);

        // 로그로 결과를 확인
        log.info("Admin requested MeongStoryBoardList: {}", result);

        // 모델에 결과 데이터를 추가
        model.addAttribute("result", result);
        model.addAttribute("pageRequestDTO", pageRequestDTO);

        return "admin/getMeongStoryBoardList";
    }

    @GetMapping("/storyread")
    public String storylistread(@RequestParam("seq") long seq,
                                @ModelAttribute("requestDTO") PageRequestDTO requestDTO,
                                Model model) {

        // seq 값에 해당하는 게시글 상세 정보를 가져옴
        MeongStoryDTO dto = service.read(seq);

        // dto를 모델에 추가하여 뷰로 전달
        model.addAttribute("dto", dto);

        // 게시글 상세보기 페이지로 이동
        return "admin/getMeongStoryBoardRead";
    }


    @PostMapping("/remove")
    public String remove(long seq,
                         RedirectAttributes redirectAttributes,
                         Model model){
        log.info("seq: " + seq);

        service.remove(seq);

        redirectAttributes.addFlashAttribute("msg", seq);

        return "redirect:/admin/getMeongStoryBoardList";
    }

}
