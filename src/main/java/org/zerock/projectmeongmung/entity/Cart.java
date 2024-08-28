package org.zerock.projectmeongmung.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cart")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Cart {

    @EmbeddedId
    private CartId id;

    @MapsId("userId")
    @ManyToOne
    @JoinColumn(name = "id", nullable = false)
    private User user;

    @MapsId("pID")
    @ManyToOne
    @JoinColumn(name = "pID", nullable = false)
    private Product product;

    @Column(name = "amount", nullable = false)
    private int amount;

    @Column(name = "price", nullable = false)
    private int price;

    @Builder
    public Cart(User user, Product product, int amount, int price) {
        this.id = new CartId(user.getId(), product.getPid());
        this.user = user;
        this.product = product;
        this.amount = amount;
        this.price = price;
    }
}

