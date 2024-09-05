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

    @GetMapping("/productMain")
    public String product(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "product/productMain";
    }

    // 상품 관리자 페이지 (상품 등록, 목록보기, 수정, 삭제)
    @GetMapping("/getproductlist")
    public String getProductList(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "product/getproductList";
    }

    // 상품 등록, 수정 관리자 페이지
    @GetMapping("/createProduct")
    public String createProductForm(@RequestParam(value = "productId", required = false) Long productId, Model model) {
        if (productId != null) {
            // productId가 있는 경우, 해당 상품을 조회해서 모델에 추가
            ProductDTO productDTO = productService.getProductById(productId);
            model.addAttribute("product", productDTO);  // 모델에 상품 정보를 추가
        } else {
            // productId가 없는 경우, 새로운 상품 등록 폼을 보여줌
            model.addAttribute("product", new ProductDTO());  // 빈 DTO를 모델에 추가
        }
        return "product/create_product";
    }


    // 상품 올리기
    @PostMapping("/createProduct")
    public String creatProduct(

            @RequestParam("productFile") MultipartFile productFile,
            @RequestParam("pname") String pname,
            @RequestParam("pprice") int pprice,
            @RequestParam("pcategory") String pcategory,
            @RequestParam("pdescription") String pdescription,
            @RequestParam("pcompany") String pcompany,
            @RequestParam("pstock") int pstock) throws IOException {

        // 파일올리기
        String productphoto = fileService.saveFile(productFile);

        // 제품 저장
        productService.saveProduct(productphoto, pname, pprice, pcategory, pdescription, pcompany, pstock);

        return "redirect:/productMain";
    }

    // 상품 수정
    @PostMapping("/updateProduct")
    public String updateProduct(
            @RequestParam("productId") Long productId,
            @RequestParam(value = "productFile", required = false) MultipartFile productFile,
            @RequestParam("pname") String pname,
            @RequestParam("pprice") int pprice,
            @RequestParam("pcategory") String pcategory,
            @RequestParam("pdescription") String pdescription,
            @RequestParam("pcompany") String pcompany,
            @RequestParam("pstock") int pstock) throws IOException {

        // 기존 Product를 데이터베이스에서 조회
        Product existingProduct = productService.getProductById(productId).toEntity();

        // 파일올리기
        String productphoto;
        if (productFile != null && !productFile.isEmpty()) {
            // 새로운 파일을 업로드하는 경우
            productphoto = fileService.saveFile(productFile);
        } else {
            // 파일을 업로드하지 않은 경우 기존 파일을 유지
            productphoto = existingProduct.getProductphoto();
        }

        productService.updateProduct(productId, productphoto, pname, pprice, pcategory, pdescription, pcompany, pstock);
        return "redirect:/productMain";
    }

    // 상품 삭제하기
    @PostMapping("/deleteProduct")
    public String deleteProduct(@RequestParam("productId") Long productId) {
        productService.productDelete(productId);
        return "redirect:/productMain";
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

