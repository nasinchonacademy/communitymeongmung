package org.zerock.projectmeongmung.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zerock.projectmeongmung.dto.ProductDTO;
import org.zerock.projectmeongmung.entity.Product;
import org.zerock.projectmeongmung.repository.ProductRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

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

        productRepository.save(product);
    }

    // 모든 제품 가지고 오기
    public List<Product> getAllProducts() {
        return productRepository.findAll(); // DB에서 모든 제품 가지고 오기
    }

    // 카테고리별 제품 조회
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByPcategory(category);
    }

    // 제품 업데이트
    public void updateProduct(Long productId, String productphoto, String pname, int pprice, String pcategory, String pdescription, String pcompany, int pstock) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + productId));

        product.setPname(pname);
        product.setPprice(pprice);
        product.setPcategory(pcategory);
        product.setPdescription(pdescription);
        product.setPcompany(pcompany);
        product.setPstock(pstock);
        product.setProductphoto(productphoto);

        productRepository.save(product);
    }


    // 제품 삭제
    public void productDelete(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new IllegalArgumentException("Product not found with ID: " + productId);
        }
        productRepository.deleteById(productId);
    }

    public ProductDTO getProductById(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + productId));

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

    public List<ProductDTO> getTop3StoriesByPid() {
        // Product 리스트를 가져옵니다.
        List<Product> topProducts = productRepository.findTop3ByOrderByPidDesc();
        // Product 리스트를 ProductDTO 리스트로 변환하여 반환합니다.
        return topProducts.stream()
                .map(this::entityToDto) // Product를 ProductDTO로 변환
                .collect(Collectors.toList());
    }

    private ProductDTO entityToDto(Product product) {
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
