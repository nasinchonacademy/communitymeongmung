package org.zerock.projectmeongmung.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.projectmeongmung.entity.Cart;
import org.zerock.projectmeongmung.entity.CartId;

public interface CartRepository extends JpaRepository<Cart, CartId> {
}
