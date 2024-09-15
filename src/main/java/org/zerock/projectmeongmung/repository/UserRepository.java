package org.zerock.projectmeongmung.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.projectmeongmung.entity.User;
import org.zerock.projectmeongmung.entity.Vet;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUid(String uid);//uid로 사용자 정보를 가져옴
    Optional<User> findByNickname(String nickname);
    Optional<User> findByEmail(String email);
    Optional<User> findById(Long userId);

    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.nickname = :nickname, u.dogname = :dogname, u.dogbreed = :dogbreed, u.profilePhoto = :profilePhoto, u.dogbirthday = :dogbirthday WHERE u.uid = :uid")
    void updateUser(@Param("uid") String uid,
                    @Param("nickname") String nickname,
                    @Param("dogname") String dogname,
                    @Param("profilePhoto") String profilePhoto,
                    @Param("dogbreed") String dogbreed,
                    @Param("dogbirthday") Date dogbirthday);


    Optional<User> findByResetToken(String token);  // 비밀번호 재설정 토큰으로 유저 조회



}
