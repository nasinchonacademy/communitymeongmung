package org.zerock.projectmeongmung.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.zerock.projectmeongmung.entity.User;
import org.zerock.projectmeongmung.service.UserService;

@Controller
@RequestMapping("/game_list")
public class GameController {

    private final UserService userService;

    public GameController(UserService userService) {
        this.userService = userService;
    }

    // 게임 목록 페이지로 이동
    @GetMapping
    public String game_list(Model model){
        return "game/game_list";
    }

    @GetMapping("/game")
    public String gamePage(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUid(userDetails.getUsername()); // userService를 통해 User 객체 가져오기
        model.addAttribute("user", user);
        return "game";
    }

    // 룰렛 게임 페이지로 이동
    @GetMapping("/rollet")
    public String gameRollet(Model model) {
        return "game/game_rollet";
    }

    // 가위바위보 페이지로 이동
    @GetMapping("/rsp")
    public String gameRsp(Model model) {
        return "game/game_rsp";
    }

    // 틱택토 게임 페이지로 이동
    @GetMapping("/tic")
    public String gameTic(Model model) {
        return "game/game_tic";
    }

    // update-jelly-points
    @PostMapping("/update-jelly-points")
    public ResponseEntity<String> updateJellyPoints(@RequestParam String uid, @RequestParam int points) {
        try {
            // 포인트를 사용자에게 추가
            userService.addJellyPointsToUser(uid, points);
            return ResponseEntity.ok("포인트가 성공적으로 적립되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("포인트 적립 중 오류가 발생했습니다.");
        }
    }

}
