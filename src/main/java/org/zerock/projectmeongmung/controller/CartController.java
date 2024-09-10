package org.zerock.projectmeongmung.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;
import org.zerock.projectmeongmung.entity.Cart;
import org.zerock.projectmeongmung.entity.Product;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.zerock.projectmeongmung.entity.User;
import org.zerock.projectmeongmung.service.CartService;

import java.util.Map;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    // UserDetailsService를 주입받음
    @Autowired
    private UserDetailsService userDetailsService;

    @PostMapping("/add")
    public ResponseEntity<String> addToCart(
            @RequestBody Map<String, Object> cartData, // JSON 데이터를 받음
            Authentication authentication) {

        // UserDetailsService를 사용하여 현재 로그인한 사용자 정보 가져오기
        // User를 UserDetails로 형변환을 하러다 실패한 코드
        // User user = (User) userDetailsService.loadUserByUsername(authentication.getName());

        // authentication에서 사용자의 이름(아이디) 가져오기
        String username = authentication.getName();

        // UserRepository 등을 이용해 User 엔터티를 데이터베이스에서 조회
        User user = cartService.findUserByUsername(username); // 이 부분을 서비스 계층에서 처리

        // 사용자 정보가 없을 경우 처리
        if (user == null) {
            return ResponseEntity.status(401).body("사용자 정보가 없습니다.");
        }

        Long productId = Long.valueOf(cartData.get("productId").toString()); // productId를 받아옴
        int amount = (int) cartData.get("amount"); // 수량을 받아옴

        // 장바구니에 상품 추가
        cartService.addProductToCart(user, productId, amount);

        return ResponseEntity.ok("상품이 장바구니에 추가되었습니다.");

    }

        @GetMapping
        public String shoppingCart(Model model){
            return "mypage/mypage";
        }
}
