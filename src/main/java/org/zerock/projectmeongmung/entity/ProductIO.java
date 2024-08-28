package org.zerock.projectmeongmung.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Table(name = "productio")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ProductIO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "iono", updatable = false)
    private Long iono;

    @ManyToOne
    @JoinColumn(name = "pID", nullable = false)
    private Product product;

    @ManyToOne
    @JoinColumn(name = "orderno", nullable = false)
    private Buy buy;

    @Column(name = "amount", nullable = false)
    private int amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private Status status;

    @Column(name = "pdate")
    private Timestamp pdate;

    @Builder
    public ProductIO(Product product, Buy buy, int amount, Status status, Timestamp pdate) {
        this.product = product;
        this.buy = buy;
        this.amount = amount;
        this.status = status;
        this.pdate = pdate;
    }

    public enum Status {
        IN, OUT
    }
}

