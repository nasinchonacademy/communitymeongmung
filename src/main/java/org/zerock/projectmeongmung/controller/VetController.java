package org.zerock.projectmeongmung.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.zerock.projectmeongmung.dto.VetDTO;
import org.zerock.projectmeongmung.entity.Vet;
import org.zerock.projectmeongmung.service.VetService;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/vet")
public class VetController {

    @Autowired
    private VetService vetService;

    // 수의사 목록 출력 메서드
    @GetMapping("/list")
    public String listVets(Model model) {
        List<Vet> vets = vetService.getAllVets();
        model.addAttribute("vets", vets);
        return "meongsoshtml/VetList"; // 수의사 목록을 출력할 템플릿 파일
    }

}
