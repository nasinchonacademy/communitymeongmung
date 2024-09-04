package org.zerock.projectmeongmung.mapper;

import org.zerock.projectmeongmung.dto.BuyDTO;
import org.zerock.projectmeongmung.entity.Buy;
import org.zerock.projectmeongmung.entity.User;

import java.sql.Timestamp;

public class BuyMapper {

    // DTO를 엔티티로 변환하는 메서드
    public static Buy toEntity(BuyDTO buyDTO, User user) {
        return Buy.builder()
                .user(user)
                .resname(buyDTO.getResName())
                .resphone(buyDTO.getResPhone())
                .resaddress(buyDTO.getResAddress())
                .resrequirement(buyDTO.getResRequirement())
                .totalprice(buyDTO.getTotalPrice())
                // .orderDate(buyDTO.getOrderDate() != null ? buyDTO.getOrderDate() : new Timestamp(System.currentTimeMillis()))
                .build();
    }

    // 엔티티를 DTO로 변환하는 메서드
    public static BuyDTO toDTO(Buy buy) {
        return BuyDTO.builder()
                .resName(buy.getResname())  // 수령인 이름
                .resPhone(buy.getResphone())  // 수령인 전화번호
                .resAddress(buy.getResaddress())  // 수령인 주소
                .resRequirement(buy.getResrequirement())  // 요청 사항
                .totalPrice(buy.getTotalprice())  // 총 가격
                // .orderDate(buy.getOrderDate())  // 주문 날짜
                .build();
    }
}

