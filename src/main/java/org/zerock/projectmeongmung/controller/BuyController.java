package org.zerock.projectmeongmung.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.zerock.projectmeongmung.dto.ProductDTO;
import org.zerock.projectmeongmung.entity.Product;
import org.zerock.projectmeongmung.entity.User;
import org.zerock.projectmeongmung.service.BuyService;
import org.zerock.projectmeongmung.service.ProductService;
import org.zerock.projectmeongmung.service.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class BuyController {

    private static final Logger logger = LoggerFactory.getLogger(BuyController.class);

    @Autowired
    private ProductService productService;

    @Autowired
    private BuyService buyService;

    @Autowired
    private UserService userService; // 사용자 정보 가져오기

    @GetMapping("/get_order_list")
    public String getOderList(Model model) {
        return "shopping/get_order_list";
    }

    @GetMapping("payment")
    public String getPayment(@RequestParam("productId") Long productId, Model model) {

        // 제품 정보 가지고 오기
        ProductDTO product = productService.getProductById(productId);
        model.addAttribute("product", product);

        return "shopping/payment";
    }

    @GetMapping("/payment_complete")
    public String getPaymentComplete(@RequestParam("productId") Long productId, Model model) {

        // 제품 정보 가지고 오기
        ProductDTO product = productService.getProductById(productId);
        model.addAttribute("product", product);

        return "shopping/payment_complete";
    }

    @GetMapping("/get_order_check")
    public String getOrderCheck(Model model) {
        return "shopping/get_order_check";
    }

    @PostMapping("/submitorder")
    public String submitOrder(@RequestParam("productId") Long productId,
                              @RequestParam("resname") String resname,
                              @RequestParam("resaddress") String resaddress,
                              @RequestParam("resphone") String resphone,
                              @RequestParam("resrequirement") String resrequirement,
                              Authentication authentication,
                              Model model) {

        // 로그로 파라미터 값 출력
        logger.info("Product ID: " + productId);
        logger.info("Receiver Name: " + resname);
        logger.info("Receiver Address: " + resaddress);
        logger.info("Receiver Phone: " + resphone);
        logger.info("Requirement: " + resrequirement);

        User user = (User) authentication.getPrincipal(); // 현재 로그인된 사용자 정보

        // 제품 정보 가져오기
        Product product = productService.getProductById(productId).toEntity();

        // 총 결제 금액 계산 (예: 제품 가격)
        int totalPrice = product.getPprice(); // 젤리 차감 후 금액 추가 가능

        // 결제 정보 저장
        // buyService.createBuy(user, product, resname, resaddress, resphone, resrequirement, totalPrice);

        return "redirect:/payment_complete?productId=" + productId;

}




}
