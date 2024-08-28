package org.zerock.projectmeongmung.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserViewController {
    @GetMapping("/login")
    public String login() {
        return "member/login";
    }

    @GetMapping("/signup1")
    public String signup1() {
        return "member/signup1";
    }

    @GetMapping("/signup2")
    public String signup2() {
        return "member/signup2";
    }

}

