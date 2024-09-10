package org.zerock.projectmeongmung.controller;

import jakarta.persistence.Column;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.zerock.projectmeongmung.dto.MeongStoryDTO;
import org.zerock.projectmeongmung.dto.ProductDTO;
import org.zerock.projectmeongmung.entity.Product;
import org.zerock.projectmeongmung.entity.User;
import org.zerock.projectmeongmung.service.FileService;
import org.zerock.projectmeongmung.service.MeongStoryService;
import org.zerock.projectmeongmung.service.ProductService;
import org.zerock.projectmeongmung.service.UserDetailService;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/meongmung")
public class MeongmungController {

    @Autowired
    private UserDetailService userDetailService;

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

/*    @GetMapping("/notices")
    public String getNotices(@RequestParam("uid") String uid, Model model) {
        // uid로 사용자 찾기 (userDetailService를 통해)
        User user = userDetailService.findUserByUid(uid);

        // 해당 사용자의 notices 리스트 가져오기
        List<String> notices = user.getNotices();

        // notices 리스트를 모델에 추가하여 뷰로 전달
        model.addAttribute("notices", notices);

        // "userNotices" 뷰로 이동
        return "userNotices";  // 예를 들어, userNotices.html 파일로 연결
    }*/
}
