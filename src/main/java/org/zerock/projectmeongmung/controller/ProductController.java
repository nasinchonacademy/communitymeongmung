package org.zerock.projectmeongmung.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.projectmeongmung.entity.Product;
import org.zerock.projectmeongmung.repository.ProductRepositrory;
import org.zerock.projectmeongmung.service.ProductService;

@Controller
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/productMain")
    public String product() {
        return "product/productMain";
    }

    @GetMapping("/creatProduct")
    public String creatProduct1() {
        return "product/create_product";
    }

    @PostMapping("/creatProduct")
    public String creatProduct(MultipartFile file, String pname, int pprice, String pcaregory, String pdescription, String pcompany, int pstock) {
        // 파일올리기
        System.out.println("-------------------확인-------------------");
        return "redirect:/productMain";
    }

//    @GetMapping("/payment")
//    public String payment() {
//        return "shopping/payment";
//    }
}

