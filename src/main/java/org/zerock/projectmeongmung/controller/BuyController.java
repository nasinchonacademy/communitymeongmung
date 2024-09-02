package org.zerock.projectmeongmung.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.zerock.projectmeongmung.dto.ProductDTO;
import org.zerock.projectmeongmung.entity.Product;
import org.zerock.projectmeongmung.service.ProductService;

@Controller
public class BuyController {

    @Autowired
    private ProductService productService;

    @GetMapping("/get_order_list")
    public String getOderList(Model model){
        return "shopping/get_order_list";
    }

    @GetMapping("payment")
    public String getPayment(@RequestParam("productId") Long productId, Model model){
        // 제품 정보 가지고 오기
        ProductDTO product = productService.getProductById(productId);
        model.addAttribute("product", product);

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
