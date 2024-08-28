package org.zerock.projectmeongmung.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/shopping_cart")
public class CartController {

        @GetMapping
        public String shopping_cart(Model model){
            return "shopping/shopping_cart";
        }
}
