package org.zerock.projectmeongmung.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BuyController {
    @GetMapping("/get_order_list")
    public String getOderList(Model model){
        return "shopping/get_order_list";
    }

    @GetMapping("payment")
    public String getPayment(Model model){
        return "shopping/payment";
    }

    @GetMapping("/payment_complete")
    public String getPaymentComplete(Model model){
        return "shopping/payment_complete";
    }

    @GetMapping("/get_order_check")
    public String getOrderCheck(Model model){
        return "shopping/get_order_check";
    }

}
