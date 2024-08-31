package org.zerock.projectmeongmung.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.projectmeongmung.entity.Product;
import org.zerock.projectmeongmung.repository.ProductRepositrory;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepositrory productRepositrory;

    @Autowired
    private FileService fileService;

    public void saveProduct(String productphoto, String pname, int pprice, String pcategory, String pdescription, String pcompany, int pstock) {

        // 제품 저장
        Product product = Product.builder()
                .pname(pname)
                .pprice(pprice)
                .pcategory(pcategory)
                .pdescription(pdescription)
                .pcompany(pcompany)
                .pstock(pstock)
                .productphoto(productphoto)
                .build();

        productRepositrory.save(product);
    }

    // 모든 제품 가지고 오기
    public List<Product> getAllProducts() {
        return productRepositrory.findAll(); // DB에서 모든 제품 가지고 오기
    }
}