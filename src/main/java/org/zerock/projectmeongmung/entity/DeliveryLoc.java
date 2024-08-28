package org.zerock.projectmeongmung.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "deliveryloc")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class DeliveryLoc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "memberid", nullable = false)
    private User user;

    @Column(name = "roadaddr")
    private String roadAddr;

    @Column(name = "localaddr")
    private String localAddr;

    @Column(name = "etcaddr")
    private String etcAddr;

    @Builder
    public DeliveryLoc(User user, String roadAddr, String localAddr, String etcAddr) {
        this.user = user;
        this.roadAddr = roadAddr;
        this.localAddr = localAddr;
        this.etcAddr = etcAddr;
    }
}
