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
    public String getPayment(@RequestParam("productId") Long productId, Authentication authentication, Model model) {

        // 현재 로그인한 사용자 가져오기
        // 스프링 시큐리티
        User user = (User) authentication.getPrincipal();

        // 제품 정보 가지고 오기
        ProductDTO product = productService.getProductById(productId);
        model.addAttribute("product", product);

        // 젤리 포인트 가지고 오기
        int productPrice = product.getPprice(); // 여기가 문제라면 ProductDTO의 getter 메서드 확인 필요
        int jellyPoints = user.getJellypoint(); // User 엔티티에서 젤리 포인트 값 가지고 오기

        // 젤리 차감 후 최종 금액
        int finalPrice = Math.max(0, productPrice - jellyPoints); // 젤리 포인트를 차감한 금액, 최소 0원으로 설정

        // 모델에 젤리 포인트와 최종 결제 금액 추가
        model.addAttribute("jellyPoints", jellyPoints);
        model.addAttribute("finalPrice", finalPrice);

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

        // 사용자의 젤리 포인트 차감
        int jellyPoints = user.getJellypoint();
        int remainingPrice = Math.max(0, totalprice - jellyPoints);

        // 결제 금액 차감 후 젤리 포인트 업데이트
        user.subtractJellyPoints(totalprice); // 젤리 포인트 차감
        userService.save(user); // 업데이트된 사용자 정보 저장

        // 주문 생성 후 반환되는 주문 번호
        Buy buy = buyService.createBuy(productId, user, resname, resphone, postcode,
               roadaddress, jibunaddress, detailaddress, extraaddress, resrequirement, totalprice);

        // 주문 정보 모델에 추가
        // model.addAttribute("buy", buy);

        // return "redirect:/payment_complete_success?productId=" + productId;
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
