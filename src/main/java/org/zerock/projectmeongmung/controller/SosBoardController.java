package org.zerock.projectmeongmung.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/soshospitallist")
public class SosBoardController {

    // 기본적인 soshospitallist.html 템플릿을 반환
    @GetMapping
    public String soshospitallist() {
        return "mungSosHtml/soshospitallist";
    }

    // 첫 번째 콘텐츠를 로드하는 메서드
    @GetMapping("/content1")
    public String loadContent1(Model model) {
        // 필요한 데이터를 모델에 추가
        return "fragments/mungSos/soshospitallistContent :: content1";
    }

    // 두 번째 콘텐츠를 로드하는 메서드
    @GetMapping("/content2")
    public String loadContent2(Model model) {
        // 필요한 데이터를 모델에 추가
        return "fragments/mungSos/soshospitallistContent :: content2";
    }

    @GetMapping("/sosqnaboard")
    public String sosboard(Model model) {
        // 필요한 데이터를 모델에 추가
        return "mungSosHtml/sosqnaboard";
    }

    @GetMapping("/soswirte")
    public String soswirte(Model model) {
        // 필요한 데이터를 모델에 추가
        return "mungSosHtml/sosqnawirte";
    }
}