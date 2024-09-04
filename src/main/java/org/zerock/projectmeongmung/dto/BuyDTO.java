package org.zerock.projectmeongmung.dto;

import lombok.*;

import java.sql.Timestamp;

@Getter
@Setter
@Builder
@NoArgsConstructor // 기본 생성자
@AllArgsConstructor // 모든 필드를 포함한 생성자
public class BuyDTO {

    private Long orderno;
    private Long userId;
    private String resName;
    private String resAddress;
    private String resPhone;
    private String resRequirement;
    private int totalPrice;
    private Timestamp orderDate;

    // 생성자
    public BuyDTO(Long userId, String resName, String resAddress, String resPhone, String resRequirement, int totalPrice, Timestamp orderDate) {
        this.userId = userId;
        this.resName = resName;
        this.resAddress = resAddress;
        this.resPhone = resPhone;
        this.resRequirement = resRequirement;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
    }

}
