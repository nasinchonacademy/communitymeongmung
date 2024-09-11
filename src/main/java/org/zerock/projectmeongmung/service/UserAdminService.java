package org.zerock.projectmeongmung.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Transactional
    public void toggleUserActivation(String uid, boolean Active) {
        User user = userAdminRepository.findByUid(uid)
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + uid));

        user.setActive(Active);  // 계정 활성화 상태 변경
        userAdminRepository.save(user); // 변경 사항 저장
    }

    public Page<User> searchUsers(String keyword, String category, Pageable pageable) {
        if (keyword != null && !keyword.isEmpty()) {
            switch (category) {
                case "uid":
                    return userAdminRepository.findByUidContaining(keyword, pageable);
                case "name":
                    return userAdminRepository.findByNameContaining(keyword, pageable);
                case "email":
                    return userAdminRepository.findByEmailContaining(keyword, pageable);
                default:
                    return userAdminRepository.findAll(pageable);  // 기본적으로 모든 사용자를 반환
            }
        }
        return userAdminRepository.findAll(pageable);
    }



}
