package org.zerock.projectmeongmung.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.projectmeongmung.dto.ProductDTO;
import org.zerock.projectmeongmung.entity.Product;
import org.zerock.projectmeongmung.service.FileService;
import org.zerock.projectmeongmung.service.ProductService;

import java.io.IOException;
import java.util.List;

@Controller
public class ProductController {

    private final ProductService productService;
    private final FileService fileService;;

    @Autowired
    public ProductController(ProductService productService, FileService fileService) {
        this.productService = productService;
        this.fileService = fileService;
    }

//    @GetMapping("/productMain")
//    public String product(Model model) {
//        List<Product> products = productService.getAllProducts();
//        model.addAttribute("products", products);
//        return "product/productMain";
//    }

    // 전체 또는 카테고리별 상품 조회
    @GetMapping("/productMain")
    public String product(@RequestParam(value = "category", required = false) String category, Model model) {
        List<Product> products;

        // 카테고리 선택이 없는 경우 전체 상품을 보여줌
        if (category == null || category.isEmpty()) {
            products = productService.getAllProducts();
        } else {
            products = productService.getProductsByCategory(category);
        }

        model.addAttribute("products", products);
        model.addAttribute("selectedCategory", category);  // 선택된 카테고리를 모델에 추가

        return "product/productMain";
    }


    // 제품 상세보기
    @GetMapping("/details")
    public ResponseEntity<?> getProductDetails(@RequestParam("id") Long productId) {
        // 제품 정보 데이터베이스에서 조회
        ProductDTO productDTO = productService.getProductById(productId);

        if (productDTO == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("제품을 찾을 수 없습니다.");
        }
        
        // 제품 정보를 JSON 형식으로 반환
        return ResponseEntity.ok(productDTO);

    }
}

