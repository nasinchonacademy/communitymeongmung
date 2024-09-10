package org.zerock.projectmeongmung.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.zerock.projectmeongmung.dto.VetDTO;
import org.zerock.projectmeongmung.entity.SOSboardcomment;
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

//    @PostMapping("/recommend")
//    public String recommendVet(@RequestParam Long vetId, @RequestParam Long commentId, @RequestParam Long userId, Authentication authentication) {
//        // 현재 로그인한 사용자의 UID 가져오기
//        String currentUserUid = authentication.getName();
//
//        // 현재 로그인한 사용자의 User 정보 가져오기 (userId 포함)
//        User currentUser = userService.findByUid(currentUserUid);
//
//        if (currentUser == null) {
//            throw new RuntimeException("로그인한 사용자를 찾을 수 없습니다.");
//        }
//
//        // 추천 처리 (vetId, commentId, userId 기반)
//        vetService.recommendVet(vetId, commentId, userId, currentUser);
//
//        return "redirect:/vet/recommend/success";
//    }

    @PostMapping("/recommend")
    @ResponseBody  // AJAX로 요청한 응답을 JSON으로 반환
    public ResponseEntity<?> recommendVet(@RequestParam Long vetId, @RequestParam Long commentId, @RequestParam Long userId, Authentication authentication) {
        // 현재 로그인한 사용자 정보
        String currentUserUid = authentication.getName();

        // 현재 로그인한 사용자의 User 정보 가져오기 (userId 포함)
        User currentUser = userService.findByUid(currentUserUid);

        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("로그인한 사용자를 찾을 수 없습니다.");
        }

        // 추천 처리
        try {
            vetService.recommendVet(vetId, commentId, userId, currentUser);
            return ResponseEntity.ok("추천이 완료되었습니다.");  // 성공 메시지 반환
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


}
