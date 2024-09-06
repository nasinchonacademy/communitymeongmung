package org.zerock.projectmeongmung.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zerock.projectmeongmung.entity.VetLog;
import org.zerock.projectmeongmung.repository.VetLogRepository;
import org.zerock.projectmeongmung.entity.Vet;

import java.sql.Timestamp;
import java.util.List;

@Service
public class VetLogService {

    @Autowired
    private VetLogRepository vetLogRepository;

    // 특정 수의사에 대한 로그 저장
    public void saveVetLog(Vet vet, String message) {
        VetLog log = VetLog.builder()
                .vet(vet)
                .logMessage(message)
                .timestamp(new Timestamp(System.currentTimeMillis()))
                .build();
        vetLogRepository.save(log);
    }

    // 특정 수의사에 대한 로그 조회
    public List<VetLog> getLogsByVetId(Long vetid) {
        return vetLogRepository.findByVet_Vetid(vetid);
    }
}
