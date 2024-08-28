package org.zerock.projectmeongmung.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.projectmeongmung.entity.Product;

public interface ProductRepositrory extends JpaRepository<Product, Long> {
}
