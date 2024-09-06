package org.zerock.projectmeongmung.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zerock.projectmeongmung.dto.VetDTO;
import org.zerock.projectmeongmung.entity.Vet;
import org.zerock.projectmeongmung.repository.VetRepository;
import org.zerock.projectmeongmung.controller.FileController;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VetService {

    @Autowired
    private VetRepository vetRepository;

    @Autowired
    private FileController fileController;

    // 수의사 등록 메서드
    public Vet registerVet(VetDTO vetDTO) throws IOException {
        String profilePhotoFileName = null;

        if (vetDTO.getProfilePhoto() != null && !vetDTO.getProfilePhoto().isEmpty()) {
            // 프로필 사진을 저장하고 파일명을 가져옴
            profilePhotoFileName = fileController.saveProfilePhoto(vetDTO.getProfilePhoto());
        }

        // VetDTO를 Vet 엔티티로 변환 및 프로필 사진 파일 이름 설정
        Vet vet = Vet.builder()
                .vetname(vetDTO.getVetname())
                .animalhospitlename(vetDTO.getAnimalhospitlename())
                .registerdate(vetDTO.getRegisterdate())  // String 처리
                .withdrawaldate(vetDTO.getWithdrawaldate() != null ? Timestamp.valueOf(vetDTO.getWithdrawaldate() + " 00:00:00") : null)
                .profilePhoto(profilePhotoFileName)
                .description(vetDTO.getDescription())
                .visibility(vetDTO.getVisibility())  // 초기 공개 여부 설정
                .build();

        return vetRepository.save(vet);
    }

    // 수의사 정보 조회 메서드
    public VetDTO getVetById(Long vetid) {
        Optional<Vet> vetOpt = vetRepository.findById(vetid);
        if (vetOpt.isPresent()) {
            Vet vet = vetOpt.get();
            return VetDTO.builder()
                    .vetid(vet.getVetid())
                    .vetname(vet.getVetname())
                    .animalhospitlename(vet.getAnimalhospitlename())
                    .registerdate(vet.getRegisterdate())
                    .withdrawaldate(vet.getWithdrawaldate() != null ? vet.getWithdrawaldate().toString() : null)
                    .profilePhotoPath(vet.getProfilePhoto())
                    .description(vet.getDescription())
                    .visibility(vet.getVisibility())
                    .build();
        }
        return null;
    }

    // 수의사 엔티티 조회 메서드
    public Vet getVetEntityById(Long vetid) {
        return vetRepository.findById(vetid).orElseThrow(() -> new RuntimeException("수의사를 찾을 수 없습니다."));
    }

    // 수의사 업데이트 메서드
    public void updateVet(Long vetid, VetDTO vetDTO) throws IOException {
        Optional<Vet> vetOpt = vetRepository.findById(vetid);
        if (vetOpt.isPresent()) {
            Vet vet = vetOpt.get();

            // 기본 정보 업데이트
            vet.setVetname(vetDTO.getVetname());
            vet.setAnimalhospitlename(vetDTO.getAnimalhospitlename());
            vet.setRegisterdate(vetDTO.getRegisterdate());
            vet.setWithdrawaldate(vetDTO.getWithdrawaldate() != null ? Timestamp.valueOf(vetDTO.getWithdrawaldate() + " 00:00:00") : null);

            // 프로필 사진이 업로드된 경우 처리
            if (vetDTO.getProfilePhoto() != null && !vetDTO.getProfilePhoto().isEmpty()) {
                String newProfilePhotoFileName = fileController.saveProfilePhoto(vetDTO.getProfilePhoto());
                vet.setProfilePhoto(newProfilePhotoFileName);
            }

            // description 필드를 새로운 가변 리스트로 설정하여 업데이트
            if (vetDTO.getDescription() != null && !vetDTO.getDescription().isEmpty()) {
                vet.setDescription(new ArrayList<>(vetDTO.getDescription()));  // 새로운 가변 리스트로 교체
            } else if (vet.getDescription() != null && !vet.getDescription().isEmpty()) {
                // description이 null이거나 비어있으면 기존 값을 유지하도록 처리
                vet.setDescription(new ArrayList<>(vet.getDescription()));
            } else {
                // 기존 description이 없고 새로 입력된 것도 없으면 빈 리스트로 설정
                vet.setDescription(new ArrayList<>());
            }

            // 자동 비공개 처리 로직 적용
            autoSetVisibility(vet);

            // 저장
            vetRepository.save(vet);
        }
    }





    // 수의사 공개/비공개 상태 수동 토글 처리
    public void toggleVisibility(Long vetid) {
        Optional<Vet> vetOpt = vetRepository.findById(vetid);
        if (vetOpt.isPresent()) {
            Vet vet = vetOpt.get();
            // 수동으로 공개/비공개 상태를 변경
            vet.setVisibility(!vet.getVisibility());
            vetRepository.save(vet);
        }
    }

    // 자동 비공개 처리 로직 (기간에 따른 상태만 판단하되 수동 설정을 존중)
    private void autoSetVisibility(Vet vet) {
        LocalDate today = LocalDate.now();
        LocalDate registerDate = LocalDate.parse(vet.getRegisterdate());
        LocalDate withdrawalDate = vet.getWithdrawaldate() != null ? vet.getWithdrawaldate().toLocalDateTime().toLocalDate() : null;

        // 자동 처리 로직을 사용하되, 수동 설정된 상태를 우선시
        if (today.isBefore(registerDate) || (withdrawalDate != null && today.isAfter(withdrawalDate))) {
            vet.setVisibility(false);  // 기간에 따른 자동 비공개 처리
        }
        // 만약 수동 설정을 유지하고 싶으면, else 절을 생략하여 수동 설정을 그대로 유지함
    }


    // 수의사 목록을 가져오는 메서드
    public List<Vet> getAllVets() {
        List<Vet> vets = vetRepository.findAll();
        for (Vet vet : vets) {
            autoSetVisibility(vet);  // 목록을 불러올 때 자동으로 비공개 처리 로직 적용
        }
        return vets;
    }


    // 수의사 삭제 메서드
    public void deleteVet(Long vetid) {
        vetRepository.deleteById(vetid);
    }
}
