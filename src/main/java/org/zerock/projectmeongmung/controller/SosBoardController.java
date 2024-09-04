package org.zerock.projectmeongmung.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.zerock.projectmeongmung.dto.PageRequestDTO;
import org.zerock.projectmeongmung.dto.PageResultDTO;
import org.zerock.projectmeongmung.dto.SOSboardDTO;
import org.zerock.projectmeongmung.entity.SOSboard;
import org.zerock.projectmeongmung.service.SOSboardService;

import java.util.List;

@Controller
@RequestMapping("/soshospitallist")
public class SosBoardController {

    @Autowired
    private SOSboardService sosboardservice;

    // 기본적인 soshospitallist.html 템플릿을 반환
    @GetMapping
    public String soshospitallist() {
        return "meongsoshtml/soshospitallist";
    }

    // 첫 번째 콘텐츠를 로드하는 메서드
    @GetMapping("/content1")
    public String loadContent1(Model model) {
        // 필요한 데이터를 모델에 추가
        return "fragments/mungSos/soshospitallistContent :: content1";
    }

    // 두 번째 콘텐츠를 로드하는 메서드
    @GetMapping("/content2")
    public String searchStory(Model model, PageRequestDTO pageRequestDTO,
                              @RequestParam(value = "keyword", required = false, defaultValue = "") String keyword) {
        // 검색 결과를 가져옴 (키워드가 없으면 전체 목록 조회)
        PageResultDTO<SOSboardDTO, SOSboard> result = sosboardservice.searchByKeyword(pageRequestDTO, keyword);

        // 모델에 결과 추가
        model.addAttribute("result", result);
        model.addAttribute("pageRequestDTO", pageRequestDTO);

        // 프래그먼트만 반환 (AJAX로 이 프래그먼트가 불러와짐)
        return "fragments/mungSos/soshospitallistContent :: content2";
    }

    @GetMapping("/sosqnaboard")
    public String sosboard(Model model) {
        // 필요한 데이터를 모델에 추가
        return "meongsoshtml/sosqnaboard";
    }

    @GetMapping("/soswirte")
    public String soswirte(Model model) {
        // 필요한 데이터를 모델에 추가
        return "meongsoshtml/sosqnawirte";
    }
}