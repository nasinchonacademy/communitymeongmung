package org.zerock.projectmeongmung.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.projectmeongmung.entity.VetLog;

import java.util.List;

public interface VetLogRepository extends JpaRepository<VetLog, Long> {
    List<VetLog> findByVet_Vetid(Long vetid);  // 수의사 ID로 로그 조회
}
