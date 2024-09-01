package org.zerock.projectmeongmung.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.projectmeongmung.entity.Product;
import org.zerock.projectmeongmung.repository.ProductRepositrory;
import org.zerock.projectmeongmung.service.ProductService;

import java.io.IOException;
import java.util.UUID;

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
    public String creatProduct(

            @RequestParam("productFile") MultipartFile productFile,
            @RequestParam("pname") String pname,
            @RequestParam("pprice") int pprice,
            @RequestParam("pcategory") String pcategory,
            @RequestParam("pdescription") String pdescription,
            @RequestParam("pcompany") String pcompany,
            @RequestParam("pstock") int pstock)  throws IOException {
        // 파일올리기
        System.out.println("-------------------확인-------------------");


//        // 저장할 경로 설정
//         /Users/yunakang/uploads/
        String uploadDir = "/Users/yunakang/uploads/";
//
//        // 파일명을 고유하게 하기 위해 UUID를 사용하거나 다른 로직을 사용할 수 있습니다.
        String productphoto = UUID.randomUUID().toString() + "_" + productFile.getOriginalFilename();
//
//        // 전체 경로 생성
        String filePath = uploadDir + productphoto;
//
//        // 파일을 저장할 경로로 전송
        productFile.transferTo(new java.io.File(filePath));

        System.out.println(productphoto);

        productService.saveProduct(productphoto, pname, pprice, pcategory, pdescription, pcompany, pstock);

        return "redirect:/productMain";
    }

//    @GetMapping("/payment")
//    public String payment() {
//        return "shopping/payment";
//    }
}

