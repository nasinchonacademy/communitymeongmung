package org.zerock.projectmeongmung.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.projectmeongmung.entity.Notice;
import org.zerock.projectmeongmung.entity.User;

import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    // 특정 사용자의 알림 메시지 리스트만 가져오기
    @Query("SELECT n.message FROM Notice n WHERE n.user = :user")
    List<String> findMessagesByUser(@Param("user") User user);
}
