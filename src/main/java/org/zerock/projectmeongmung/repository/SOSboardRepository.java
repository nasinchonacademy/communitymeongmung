package org.zerock.projectmeongmung.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.projectmeongmung.entity.SOSboard;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SOSboardRepository extends JpaRepository<SOSboard, Long> {
    Page<SOSboard> findByTitleContainingOrContentContaining(String title, String content, Pageable pageable);
    List<SOSboard> findTop3ByOrderByLikecountDesc(Pageable pageable);
    List<SOSboard> findByUserId(Long userId);

    @Modifying
    @Transactional
    @Query("UPDATE SOSboard m SET m.likecount = m.likecount + 1 WHERE m.sosboardseq = :sosboardseq")
    void incrementLikeCount(@Param("sosboardseq") Long sosboardseq);

}