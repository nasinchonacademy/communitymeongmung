package org.zerock.projectmeongmung.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

// JPA에서 복합 키를 처리하기 위해 @Embeddable 사용
// 독립적으로 식별되지 않고 다른 엔티티에 내장되어 사용
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
// Serializable 인터페이스
// 복합 키 클래스는 반드시 Serializable 인터페이스 구현을 해야한다
// JPA에서 복합키를 식별자로 사용하는 경우, 객체의 직렬화 필요
// 객체의 직렬화 : 자바 시스템 내부에서 사용되는 객체 또는 데이터를 외부의 자바 시스템에서도
// 사용할 수 있도록 바이트(byte) 형태로 데이터를 변환하는 기술
// 자바 시스템 간의 데이터 교환을 위해 사용
public class CartId implements Serializable {

    // 엔티티 복합 키 구성하는 필드

    // userId는 User엔티티의 식별자
    private Long userId;
    // pID는 Product엔티티의 식별자
    private Long pID;
}

