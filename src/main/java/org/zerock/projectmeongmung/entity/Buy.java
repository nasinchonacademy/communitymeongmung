package org.zerock.projectmeongmung.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "buy")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Buy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orderno")
    private Long orderno;

    @ManyToOne
    @JoinColumn(name = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "productid")  // Product와의 연관관계 추가
    private Product product;

    @Column(name = "resname")
    private String resname;

    @Column(name = "resphone")
    private String resphone;

    @Column(name = "postcode")
    private int postcode;

    @Column(name = "roadaddress")
    private String roadaddress;

    @Column(name = "jibunaddress")
    private String jibunaddress;

    @Column(name = "detailaddress")
    private String detailaddress;

    @Column(name = "extraaddress")
    private String extraaddress;

    @Column(name = "resrequirement" )
    private String resrequirement;

    @Column(name = "totalprice")
    private int totalprice;

//    @CreationTimestamp
//    @Column(name = "orderDate", nullable = false, updatable = false)
//    private Timestamp orderDate;

    @Builder
    public Buy(User user, Product product, String resname, String resphone,
               int postcode, String roadaddress, String jibunaddress, String detailaddress, String extraaddress,
               String resrequirement, int totalprice) {
        this.user = user;
        this.product = product;
        this.resname = resname;
        this.resphone = resphone;
        this.postcode = postcode;
        this.roadaddress = roadaddress;
        this.jibunaddress = jibunaddress;
        this.detailaddress = detailaddress;
        this.extraaddress = extraaddress;
        this.resrequirement = resrequirement;
        this.totalprice = totalprice;
//        this.orderDate = orderDate;  // null일 경우 현재 시간으로 설정
    }

    @Column(name = "status")
    private String status = "준비 중";

    public void setStatus(String string) {
        this.status = status;
    }
}

