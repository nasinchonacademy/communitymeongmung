package org.zerock.projectmeongmung.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.projectmeongmung.entity.*;

import java.time.LocalDate;
import java.util.List;

public interface SOSBoardLikeRepository extends JpaRepository<SOSboardlikecount, Long> {
    List<SOSboardlikecount> findByMember(User member);
    boolean existsByMemberAndSosboardAndLikecountupdate(User member, SOSboard sosboard, LocalDate likecountupdate);
}
