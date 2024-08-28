package org.zerock.projectmeongmung.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/meongmung")
public class MeongmungController {

    @GetMapping
    public String meongmung() {
        return "meongmung"; // meongmung.html 템플릿을 반환
    }
}