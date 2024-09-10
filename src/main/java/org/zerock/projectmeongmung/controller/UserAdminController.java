package org.zerock.projectmeongmung.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.zerock.projectmeongmung.entity.User;
import org.zerock.projectmeongmung.service.UserAdminService;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class UserAdminController {

    private final UserAdminService userAdminService;

    // 전체 회원 목록 조회 페이지로 이동
    @GetMapping("/users")
    public String getAllUsers(Model model) {
        List<User> userList = userAdminService.getAllUsers();
        model.addAttribute("userList", userList);
        return "admin/adminUserlist";  // userList.html 타임리프 템플릿으로 이동
    }

    // 회원 삭제
    @DeleteMapping("/users/{id}")
    public String deleteUser(@PathVariable Long id) {
        userAdminService.deleteUser(id);
        return "redirect:/admin/users";  // 삭제 후 리스트 페이지로 리다이렉션
    }

    // 관리자 권한 변경
    @PatchMapping("/users/{id}/admin")
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
