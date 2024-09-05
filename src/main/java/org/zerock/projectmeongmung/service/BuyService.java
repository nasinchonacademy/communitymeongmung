package org.zerock.projectmeongmung.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.projectmeongmung.entity.Buy;
import org.zerock.projectmeongmung.entity.Product;
import org.zerock.projectmeongmung.entity.User;
import org.zerock.projectmeongmung.repository.BuyRepository;
import org.zerock.projectmeongmung.repository.ProductRepository;

@Service
@RequiredArgsConstructor
public class BuyService {

    @Autowired
    private final BuyRepository buyRepository;
    private final ProductRepository productRepository;  // ProductRepository를 추가

    // 주문 정보 저장 메서드
    @Transactional
    public Buy createBuy(Long productId, User user, String resname, String resphone, int postcode, String roadaddress, String jibunaddress, String detailaddress, String extraaddress, String resrequirement, int totalprice) {
        // 1. 먼저 productId로 해당 Product가 존재하는지 확인
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        Buy buy = Buy.builder()
                .user(user)
                .product(existingProduct)  // 조회한 Product 사용
                .resname(resname)
                .resphone(resphone)
                .postcode(postcode)
                .roadaddress(roadaddress)
                .jibunaddress(jibunaddress)
                .detailaddress(detailaddress)
                .extraaddress(extraaddress)
                .resrequirement(resrequirement)
                .totalprice(totalprice)
//                .orderDate(new Timestamp(System.currentTimeMillis()))  // 현재 시간 저장
                .build();

        buyRepository.save(buy);
        return buy;
    }

    public Buy getOrderById(Long orderNo) {
        return buyRepository.findById(orderNo)
                .orElseThrow(() -> new IllegalArgumentException("주문번호를 조회하지 못했습니다."));
    }
}
