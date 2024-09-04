package org.zerock.projectmeongmung.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.projectmeongmung.dto.VetDTO;
import org.zerock.projectmeongmung.entity.Vet;
import org.zerock.projectmeongmung.repository.VetRepository;
import org.zerock.projectmeongmung.controller.FileController;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.List;

@Service
public class VetService {

    @Autowired
    private VetRepository vetRepository;

    @Autowired
    private FileController fileController;

    public Vet registerVet(VetDTO vetDTO) throws IOException {
        // 프로필 사진 저장
        String profilePhotoFileName = fileController.saveProfilePhoto(vetDTO.getProfilePhoto());

        // VetDTO를 Vet 엔티티로 변환 및 프로필 사진 파일 이름 설정
        Vet vet = Vet.builder()
                .vetname(vetDTO.getVetname())
                .animalhospitlename(vetDTO.getAnimalhospitlename())
                .registerdate(vetDTO.getRegisterdate())
                .withdrawaldate(vetDTO.getWithdrawaldate() != null ? Timestamp.valueOf(vetDTO.getWithdrawaldate() + " 00:00:00") : null)
                .profilePhoto(profilePhotoFileName)
                .visibility(vetDTO.getVisibility())
                .description(vetDTO.getDescription())
                .build();

        // 수의사 정보 저장
        return vetRepository.save(vet);
    }

    public List<Vet> getAllVets() {
        return vetRepository.findAll();
    }

}
