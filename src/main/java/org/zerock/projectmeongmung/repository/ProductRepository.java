package org.zerock.projectmeongmung.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.projectmeongmung.dto.ProductDTO;
import org.zerock.projectmeongmung.entity.MeongStory;
import org.zerock.projectmeongmung.entity.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
// 기본적인 CRUD 메서드는 JpaRepository에서 제공
    List<Product> findByPcategory(String pcategory);  // 카테고리별 상품 조회
    List<Product> findTop3ByOrderByPidDesc();
}
