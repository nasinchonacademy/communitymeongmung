package org.zerock.projectmeongmung.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.projectmeongmung.entity.Notice;
import org.zerock.projectmeongmung.entity.User;

import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    // 특정 사용자의 알림 메시지 리스트만 가져오기 (내림차순 정렬)
    @Query("SELECT n.message FROM Notice n WHERE n.user = :user ORDER BY n.user.id ASC")
    List<String> findMessagesByOrderByUserIdAsc(@Param("user") User user);

    // 특정 알림 삭제
    @Modifying
    @Transactional
    @Query("DELETE FROM Notice n WHERE n.id = :id")
    void removeNoticeById(@Param("id") Long id);

    @Query("SELECT n FROM Notice n WHERE n.user = :user")
    List<Notice> findByUser(@Param("user") User user);
}
