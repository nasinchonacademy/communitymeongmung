package org.zerock.projectmeongmung.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.projectmeongmung.dto.PageRequestDTO;
import org.zerock.projectmeongmung.dto.SOSboardDTO;
import org.zerock.projectmeongmung.entity.User;
import org.zerock.projectmeongmung.repository.UserRepository;
import org.zerock.projectmeongmung.service.SOSboardService;
import org.zerock.projectmeongmung.service.UserDetailService;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Log4j2
public class SOSboardController {

    @Autowired
    private UserDetailService userDetailService;

    private final SOSboardService sosboardService;
    private final UserRepository userRepository;
    private final FileController fileController;


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

        // 1페이지일 때만 조회수가 높은 3개 게시물을 추가로 가져옴

            List<SOSboardDTO> top3Boards = sosboardService.getTop3BylikeCount();
            model.addAttribute("top3Boards", top3Boards);  // 조회수가 높은 게시물


        model.addAttribute("current", current);// 라디오 버튼 상태를 모델에 추가
        System.out.println(current);



        if ("radio1".equals(current)) {
            // 동물병원 리스트 페이지 처리 (soshospitallist.html)
            return "meongsoshtml/soshospitallist";  // 동물병원 리스트 페이지
        } else {
            // SOS 상담 리스트 페이지 처리 및 페이징, 검색 처리 (soshospitallist2.html)
            model.addAttribute("result", sosboardService.searchByKeyword(pageRequestDTO, keyword != null ? keyword : ""));
            model.addAttribute("pageRequestDTO", pageRequestDTO);  // 검색과 페이징 정보를 전달
            return "meongsoshtml/soshospitallist2";  // SOS 상담 리스트 페이지
        }
    }

    @GetMapping("/sosboardwrite")
    public String sosBoardWrite(Model model, Authentication authentication,
                                @RequestParam(value = "current", defaultValue = "radio2") String current ){
        String username = authentication.getName();
        User user = userDetailService.loadUserByUsername(username);
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


}
