package org.zerock.projectmeongmung.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.projectmeongmung.entity.User;
import org.zerock.projectmeongmung.service.UserAdminService;

import java.util.List;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class UserAdminController {

    private final UserAdminService userAdminService;

    // 전체 회원 목록 조회
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userAdminService.getAllUsers();
    }

    // 회원 삭제
    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userAdminService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    // 관리자 권한 변경
    @PatchMapping("/users/{id}/admin")
    public ResponseEntity<Void> changeAdminStatus(@PathVariable Long id, @RequestParam boolean isAdmin) {
        userAdminService.changeAdminStatus(id, isAdmin);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/toggle-status/{userId}")
    public String toggleUserStatus(@PathVariable Long userId, @RequestParam boolean isActive, RedirectAttributes redirectAttributes) {
        userAdminService.updateUserStatus(userId, isActive);
        String status = isActive ? "활성화" : "비활성화";
        redirectAttributes.addFlashAttribute("message", "회원이 " + status + "되었습니다.");
        return "redirect:/admin/users";
    }






}
