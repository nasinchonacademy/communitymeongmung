package org.zerock.projectmeongmung.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.zerock.projectmeongmung.dto.ProductDTO;
import org.zerock.projectmeongmung.entity.Buy;
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

    @PostMapping("/payment_complete")
    public String submitOrder(@RequestParam("productId") Long productId,
                              @RequestParam("resname") String resname,
                              @RequestParam("resphone") String resphone,
                              @RequestParam("postcode") int postcode,
                              @RequestParam("roadaddress") String roadaddress,
                              @RequestParam("jibunaddress") String jibunaddress,
                              @RequestParam("detailaddress") String detailaddress,
                              @RequestParam("extraaddress") String extraaddress,
                              @RequestParam("resrequirement") String resrequirement,
                              @RequestParam("totalprice") int totalprice,
                              Authentication authentication,
                              Model model) {

        User user = (User) authentication.getPrincipal(); // 현재 로그인된 사용자 정보

        // 제품 정보 가지고 오기
        ProductDTO product = productService.getProductById(productId);
        model.addAttribute("product", product);

        // 결제 정보 저장
        // 결제 정보 저장
        Buy buy = buyService.createBuy(productId, user, resname, resphone, postcode,
                roadaddress, jibunaddress, detailaddress, extraaddress, resrequirement, totalprice);

        // 주문 정보 모델에 추가
        model.addAttribute("buy", buy);

        // return "redirect:/payment_complete_success?productId=" + productId;
        // 이제 buy 엔티티에서 정보를 가져와야 한다. 주문 번호로 조회하기
        return "redirect:/payment_complete_success?orderNo=" + buy.getOrderno();

    }

    // GET 방식으로 결제 완료 페이지 렌더링
//    @GetMapping("/payment_complete_success")
//    public String paymentCompleteSuccess(@RequestParam("productId") Long productId, Model model) {
//        ProductDTO product = productService.getProductById(productId);
//        model.addAttribute("product", product);
//        return "shopping/payment_complete";  // 결제 완료 페이지로 렌더링
//    }

    @GetMapping("/payment_complete_success")
    public String paymentCompleteSuccess(@RequestParam("orderNo") Long orderNo, Model model) {
        Buy buy = buyService.getOrderById(orderNo); // 주문 정보 가져오기
        model.addAttribute("buy", buy); // 주문 정보 모델에 추가
        return "shopping/payment_complete";  // 결제 완료 페이지로 렌더링
    }



}
