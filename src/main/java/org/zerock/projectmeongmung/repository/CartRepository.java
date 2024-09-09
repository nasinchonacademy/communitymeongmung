package org.zerock.projectmeongmung.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.projectmeongmung.entity.Cart;
import org.zerock.projectmeongmung.entity.CartId;
import org.zerock.projectmeongmung.entity.User;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, CartId> {
    List<Cart> findByUser(User user);
}
