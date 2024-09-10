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
    public String gameRollet(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUid(userDetails.getUsername());

        // 사용자가 오늘 이미 게임을 했는지 확인
        if (userService.hasPlayedToday(user.getUid(), "rollet")) {
            model.addAttribute("message", "오늘은 이미 게임을 하셨습니다. 내일 다시 시도해주세요.");
            return "game/game_list"; // 이미 게임을 한 경우, 목록 페이지로 리다이렉션
        }

        model.addAttribute("user", user);
        return "game/game_rollet";
    }

    // 가위바위보 페이지로 이동
    @GetMapping("/rsp")
    public String gameRsp(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUid(userDetails.getUsername());

//        // 오늘 이미 게임을 했는지 확인
//        if (userService.hasPlayedToday(user.getUid(), "rsp")) {
//            model.addAttribute("message", "오늘은 이미 게임을 하셨습니다. 내일 다시 시도해주세요.");
//            return "game/game_list"; // 이미 게임을 한 경우, 목록 페이지로 리다이렉션
//        }
        model.addAttribute("user", user);
        return "game/game_rsp";
    }

    // 틱택토 게임 페이지로 이동
    @GetMapping("/tic")
    public String gameTic(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.findByUid(userDetails.getUsername());

        // 오늘 이미 게임을 했는지 확인
        if (userService.hasPlayedToday(user.getUid(), "tic")) {
            model.addAttribute("message", "오늘은 이미 게임을 하셨습니다. 내일 다시 시도해주세요.");
            return "game/game_list"; // 이미 게임을 한 경우, 목록 페이지로 리다이렉션
        }

        model.addAttribute("user", user);
        return "game/game_tic";
    }

    // update-jelly-points
    @PostMapping("/update_jelly_points")
    public ResponseEntity<String> updateJellyPoints(@RequestParam String uid,
                                                    @RequestParam int points,
                                                    @RequestParam String gameType) {

        System.out.println("받은 uid: " + uid);
        System.out.println("받은 포인트: " + points);
        System.out.println("게임 타입: " + gameType);

        try {
            // 포인트를 사용자에게 추가
            userService.updateGamePoints(uid, gameType, points, 0); // 포인트 적립 및 기록 업데이트
            return ResponseEntity.ok("포인트가 성공적으로 적립되었습니다.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("포인트 적립 중 오류가 발생했습니다.");
        }
    }

}
