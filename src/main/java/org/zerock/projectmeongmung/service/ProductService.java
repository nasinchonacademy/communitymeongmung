package org.zerock.projectmeongmung.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.projectmeongmung.dto.ProductDTO;
import org.zerock.projectmeongmung.entity.Product;
import org.zerock.projectmeongmung.repository.ProductRepositrory;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.zerock.projectmeongmung.entity.QProduct.product;

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

    // 제품 업데이트
    public void updateProduct(Long productId, String productphoto, String pname, int pprice, String pcategory, String pdescription, String pcompany, int pstock) {
        Product product = productRepositrory.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + productId));

        product.setPname(pname);
        product.setPprice(pprice);
        product.setPcategory(pcategory);
        product.setPdescription(pdescription);
        product.setPcompany(pcompany);
        product.setPstock(pstock);
        product.setProductphoto(productphoto);

        productRepositrory.save(product);
    }


    // 제품 삭제
    public void productDelete(Long productId) {
        if (!productRepositrory.existsById(productId)) {
            throw new IllegalArgumentException("Product not found with ID: " + productId);
        }
        productRepositrory.deleteById(productId);
    }

    public ProductDTO getProductById(Long productId) {
        Product product = productRepositrory.findById(productId).orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + productId));

        if (product == null) {
            return null;  // 혹은 예외 처리
        }

        // 엔티티를 DTO로 변환하여 반환
        return new ProductDTO(
                product.getPid(),
                product.getPname(),
                product.getPprice(),
                product.getPcategory(),
                product.getPdescription(),
                product.getPcompany(),
                product.getPstock(),
                product.getProductphoto()
        );
    }
}
