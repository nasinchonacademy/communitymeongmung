package org.zerock.projectmeongmung.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.projectmeongmung.entity.Cart;
import org.zerock.projectmeongmung.entity.CartId;
import org.zerock.projectmeongmung.entity.Product;
import org.zerock.projectmeongmung.entity.User;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, CartId> {
    List<Cart> findByUser(User user);

    Optional<Cart> findByUserAndProduct_Pid(User user, Long pid);
}
