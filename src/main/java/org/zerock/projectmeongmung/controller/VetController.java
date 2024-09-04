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

    // 수의사 등록 페이지로 이동하는 메서드
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("vetDTO", new VetDTO());
        return "meongsoshtml/VetEnrollment"; // 템플릿 파일 이름 (e.g., vet-register.html)
    }

    // 수의사 등록을 처리하는 메서드
    @PostMapping("/register")
    public String registerVet(VetDTO vetDTO) {
        try {
            String descriptionText = vetDTO.getDescription().get(0); // 텍스트 영역에서의 내용을 가져옴
            List<String> descriptions = Arrays.asList(descriptionText.split("\n"));
            vetDTO.setDescription(descriptions);

            vetService.registerVet(vetDTO);
        } catch (IOException e) {
            // 파일 저장 중 오류가 발생한 경우 처리
            e.printStackTrace();
            return "redirect:/vet/register?error";
        }
        return "redirect:/soshospitallist";
    }

    // 수의사 목록 출력 메서드
    @GetMapping("/list")
    public String listVets(Model model) {
        List<Vet> vets = vetService.getAllVets();
        model.addAttribute("vets", vets);
        return "meongsoshtml/VetList"; // 수의사 목록을 출력할 템플릿 파일
    }
}
