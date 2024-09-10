package org.zerock.projectmeongmung.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.projectmeongmung.entity.User;
import org.zerock.projectmeongmung.repository.UserAdminRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserAdminService {

    private final UserAdminRepository userAdminRepository;

    // 전체 회원 목록 조회
    public List<User> getAllUsers() {
        return userAdminRepository.findAll();
    }

    // 특정 회원 정보 조회
    public User getUserById(Long userId) {
        return userAdminRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("해당 회원을 찾을 수 없습니다."));
    }

    // 회원 삭제
    public void deleteUser(Long userId) {
        userAdminRepository.deleteById(userId);
    }

    // 관리자 권한 부여/제거
    @Transactional
    public void changeAdminStatus(Long userId, boolean isAdmin) {
        User user = getUserById(userId);
        user.setAdmin(isAdmin);
        userAdminRepository.save(user);
    }

    public void updateUserStatus(Long userId, boolean isActive) {
        User user = userAdminRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("해당 회원을 찾을 수 없습니다."));
        user.setActive(isActive);
        userAdminRepository.save(user);
    }



}
