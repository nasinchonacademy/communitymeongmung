package org.zerock.projectmeongmung.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.zerock.projectmeongmung.entity.Product;
import org.zerock.projectmeongmung.repository.ProductRepositrory;

@Controller
public class ProductController {

    @Autowired
    ProductRepositrory productRepositrory;

    @GetMapping("/productMain")
    public String product() {
        return "product/productMain";
    }

    @GetMapping("/creatProduct")
    public String creatProduct1() {
        return "product/create_product";
    }

    @PostMapping("/creatProduct")
    public String creatProduct() {
        // 파일올리기
        System.out.println("-------------------확인-------------------");
        Product product = new Product("강아지사료", 5000, "간식", "냠", "냠냠", 3);
        productRepositrory.save(product);
        return "product/productMain";
    }

//    @GetMapping("/payment")
//    public String payment() {
//        return "shopping/payment";
//    }
}

