package org.zerock.projectmeongmung.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/game_list")
public class GameController {

    // 게임 목록 페이지로 이동
    @GetMapping
    public String game_list(Model model){
        return "game/game_list";
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


}
