package org.zerock.projectmeongmung.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.zerock.projectmeongmung.dto.VetDTO;
import org.zerock.projectmeongmung.entity.User;
import org.zerock.projectmeongmung.entity.Vet;
import org.zerock.projectmeongmung.repository.VetRepository;
import org.zerock.projectmeongmung.service.UserService;
import org.zerock.projectmeongmung.service.VetService;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/vet")
@RequiredArgsConstructor
public class VetController {

    @Autowired
    private VetService vetService;

    private final VetRepository vetRepository;
    private final UserService userService;


    // 수의사 목록 출력 메서드
    @GetMapping("/list")
    public String listVets(Model model) {
        List<Vet> vets = vetService.getAllVets();
        model.addAttribute("vets", vets);
        return "meongsoshtml/VetList"; // 수의사 목록을 출력할 템플릿 파일
    }
    @GetMapping("/byUser/{userId}")
    public ResponseEntity<Vet> getVetByUserId(@PathVariable Long userId) {
        Vet vet = vetService.getVetByUserId(userId);  // userId로 Vet 정보를 조회
        if (vet == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(vet);
    }

    @PostMapping("/recommend")
    public ResponseEntity<String> recommendVet(@RequestParam Long vetId, Authentication authentication) {
        // Authentication 객체에서 사용자 정보 가져오기
        String username = authentication.getName();
        User user = userService.findUserByUid(username);  // username을 통해 User 정보를 가져옴

        // 추천 기능 호출
        boolean result = vetService.recommendVet(user, vetId);

        if (result) {
            return ResponseEntity.ok("추천이 완료되었습니다.");
        } else {
            return ResponseEntity.badRequest().body("이미 추천하셨습니다.");
        }
    }

}
