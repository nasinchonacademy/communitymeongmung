package org.zerock.projectmeongmung.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.zerock.projectmeongmung.dto.MeongStoryDTO;
import org.zerock.projectmeongmung.dto.ProductDTO;
import org.zerock.projectmeongmung.entity.Product;
import org.zerock.projectmeongmung.service.FileService;
import org.zerock.projectmeongmung.service.MeongStoryService;
import org.zerock.projectmeongmung.service.ProductService;

import java.util.List;

@Controller
@RequestMapping("/meongmung")
public class MeongmungController {

    private final MeongStoryService meongStoryService;
    private final ProductService productService;


    @Autowired
    public MeongmungController(MeongStoryService meongStoryService, ProductService productService) {
        this.meongStoryService = meongStoryService;
        this.productService = productService;
    }

    @GetMapping
    public String meongmung(Model model) {
        // likecount 상위 10개 스토리를 가져와서 모델에 추가
        List<MeongStoryDTO> topStories = meongStoryService.getTop5StoriesByLikeCount();
        List<ProductDTO> products = productService.getTop3StoriesByPid();

        model.addAttribute("topStories", topStories);
        model.addAttribute("products", products);

        return "meongmung"; // meongmung.html 템플릿을 반환
    }
}
