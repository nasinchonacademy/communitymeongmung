package org.zerock.projectmeongmung.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.zerock.projectmeongmung.entity.User;
import org.zerock.projectmeongmung.service.UserAdminService;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class UserAdminController {

    private final UserAdminService userAdminService;

    // 전체 회원 목록 조회 페이지로 이동

    @GetMapping("/users")
    public String getUserList(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "category", required = false) String category,
            @PageableDefault(size = 10) Pageable pageable,
            Model model) {

        Page<User> userPage = userAdminService.searchUsers(keyword, category, pageable);
        model.addAttribute("userList", userPage.getContent());
        model.addAttribute("page", userPage);
        model.addAttribute("keyword", keyword);
        model.addAttribute("category", category);


        return "admin/adminUserlist";  // Thymeleaf 템플릿 파일 경로
    }

    // 회원 삭제
    @PostMapping("/users/{id}")
    public String deleteUser(@PathVariable Long id) {
        userAdminService.deleteUser(id);
        return "redirect:/admin/users";  // 삭제 후 리스트 페이지로 리다이렉션
    }

    // 관리자 권한 변경
    @PostMapping("/users/{id}/admin")
    public String changeAdminStatus(@PathVariable Long id, @RequestParam boolean isAdmin) {
        userAdminService.changeAdminStatus(id, isAdmin);
        return "redirect:/admin/users";  // 권한 변경 후 리스트 페이지로 리다이렉션
    }

    // 회원 활성화/비활성화
    @PostMapping("/toggleActivation")
    public String toggleUserActivation(@RequestParam String uid, @RequestParam boolean Active) {
        userAdminService.toggleUserActivation(uid, Active);
        return "redirect:/admin/users";  // 상태 변경 후 리스트 페이지로 리다이렉션
    }
}
