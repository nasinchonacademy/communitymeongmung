package org.zerock.projectmeongmung.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.zerock.projectmeongmung.entity.User;
import org.zerock.projectmeongmung.service.UserDetailService;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private UserDetailService userDetailService;

    @ModelAttribute
    public void addUserToModel(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getName())) {
            User user = userDetailService.findUserByUid(authentication.getName());
            if (user != null) {
                model.addAttribute("user", user);
            }
        }
    }
}
