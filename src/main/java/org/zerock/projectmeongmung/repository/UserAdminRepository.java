package org.zerock.projectmeongmung.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zerock.projectmeongmung.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserAdminRepository extends JpaRepository<User, Long> {
    Optional<User> findByUid(String uid);
    List<User> findAll(); // 모든 회원 정보 조회
    Page<User> findByUidContaining(String uid, Pageable pageable);
    Page<User> findByNameContaining(String name, Pageable pageable);
    Page<User> findByEmailContaining(String email, Pageable pageable);
}
