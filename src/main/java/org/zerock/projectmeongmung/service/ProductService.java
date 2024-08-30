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

//    @Autowired
    private ProductRepositrory productRepositrory;
//
//    public List<Product> getAllProducts(){
//       return productRepositrory.findAll();
//    }
//
//    public Optional<Product> getProducctById(Long pID) {
//        return productRepositrory.findById(pID);
//    }

    private final FileService fileService;

    public ProductService(ProductRepositrory productRepositrory, FileService fileService) {
        this.productRepositrory = productRepositrory;
        this.fileService = fileService;
    }

    public String saveProduct(MultipartFile file, String pname, int pprice, String pcategory, String pdescription, String pcompany, int pstock) {
        // 파일 저장
        String photoPath = null;
        try {
            photoPath = fileService.saveFile(file);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("File upload failed");
        }

        // 제품 저장
        Product product = Product.builder()
                .pname(pname)
                .pprice(pprice)
                .pcategory(pcategory)
                .pdescription(pdescription)
                .pcompany(pcompany)
                .pstock(pstock)
                .productphoto(photoPath)
                .build();

        productRepositrory.save(product);

        return photoPath;
    }
}