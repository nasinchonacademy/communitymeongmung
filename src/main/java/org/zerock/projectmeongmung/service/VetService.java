package org.zerock.projectmeongmung.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.projectmeongmung.dto.VetDTO;
import org.zerock.projectmeongmung.entity.User;
import org.zerock.projectmeongmung.entity.Vet;
import org.zerock.projectmeongmung.entity.VetRecommendation;
import org.zerock.projectmeongmung.repository.UserRepository;
import org.zerock.projectmeongmung.repository.VetRecommendationRepository;
import org.zerock.projectmeongmung.repository.VetRepository;
import org.zerock.projectmeongmung.controller.FileController;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class VetService {

    private final VetRepository vetRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final FileController fileController;
    private final VetRecommendationRepository vetRecommendationRepository;

    // 수의사 등록 메서드 (User와 Vet 동시에 저장)
    public Vet registerVet(VetDTO vetDTO) throws IOException {
        // 프로필 사진 저장 로직
        String profilePhotoFileName = null;
        if (vetDTO.getProfilePhoto() != null && !vetDTO.getProfilePhoto().isEmpty()) {
            profilePhotoFileName = fileController.saveProfilePhoto(vetDTO.getProfilePhoto());
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(vetDTO.getPassword());  // 예시로 이름+123으로 기본 비밀번호 설정

        // Vet 엔티티 생성
        Vet vet = Vet.builder()
                .vetname(vetDTO.getVetname())
                .animalhospitlename(vetDTO.getAnimalhospitlename())
                .registerdate(vetDTO.getRegisterdate())
                .withdrawaldate(vetDTO.getWithdrawaldate() != null ? Timestamp.valueOf(vetDTO.getWithdrawaldate() + " 00:00:00") : null)
                .email(vetDTO.getEmail())
                .profilePhoto(profilePhotoFileName)
                .description(vetDTO.getDescription())
                .visibility(vetDTO.getVisibility())
                .username(vetDTO.getUsername()) // 이메일을 사용자 ID로 사용
                .password(encodedPassword)
                .build();

        // User 엔티티에 정보 저장
        User user = User.builder()
                .uid(vetDTO.getUsername())
                .email(vetDTO.getEmail())                  // 이메일
                .nickname(vetDTO.getVetname())              // 닉네임을 수의사 이름으로 설정
                .name(vetDTO.getVetname())                  // 이름도  수의사 이름으로 설정
                .password(encodedPassword)                 // 암호화된 비밀번호
                .profilePhoto(profilePhotoFileName)        // 프로필 사진
                .vet(true)
                .build();

        // 저장
        userRepository.save(user);
        return vetRepository.save(vet);  // Vet 저장
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

    @Transactional
    public void deleteVet(String username) {
        // 수의사 삭제
        vetRepository.deleteByUsername(username);

        // User 삭제
        userRepository.deleteByUid(username);
    }


    public boolean recommendVet(User user, Long vetId) {
        // 수의사가 존재하는지 확인
        Vet vet = vetRepository.findById(vetId)
                .orElseThrow(() -> new RuntimeException("수의사를 찾을 수 없습니다."));

        // 사용자가 이미 해당 수의사를 추천했는지 확인
        boolean alreadyRecommended = vetRecommendationRepository.existsByUserAndVet(user, vet);

        if (alreadyRecommended) {
            return false; // 이미 추천한 경우 false 반환
        }

        // 새로운 추천 저장
        VetRecommendation recommendation = VetRecommendation.builder()
                .user(user)
                .vet(vet)
                .recommendDate(new Date())
                .build();

        vetRecommendationRepository.save(recommendation);

        // 수의사 추천 수 증가
        vet.setRecommendationCount(vet.getRecommendationCount() + 1);
        vetRepository.save(vet);

        return true; // 추천 성공
    }



    public Vet getVetByUserId(Long userId) {
        // Optional에서 값을 안전하게 추출
        return vetRepository.findByUserId(userId).orElse(null);
    }
}
