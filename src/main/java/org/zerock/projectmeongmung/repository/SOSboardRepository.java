package org.zerock.projectmeongmung.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.zerock.projectmeongmung.entity.SOSboard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SOSboardRepository extends JpaRepository<SOSboard, Long> {
    Page<SOSboard> findByTitleContainingOrContentContaining(String title, String content, Pageable pageable);

}