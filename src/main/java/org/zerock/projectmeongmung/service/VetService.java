package org.zerock.projectmeongmung.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.projectmeongmung.dto.VetDTO;
import org.zerock.projectmeongmung.entity.SOSboardcomment;
import org.zerock.projectmeongmung.entity.User;
import org.zerock.projectmeongmung.entity.Vet;
import org.zerock.projectmeongmung.entity.VetRecommendation;
import org.zerock.projectmeongmung.repository.SOSBoardCommentRepository;
import org.zerock.projectmeongmung.repository.UserRepository;
import org.zerock.projectmeongmung.repository.VetRecommendationRepository;
import org.zerock.projectmeongmung.repository.VetRepository;
import org.zerock.projectmeongmung.controller.FileController;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class VetService {

    private final VetRepository vetRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final FileController fileController;
    private final VetRecommendationRepository vetRecommendationRepository;
    private final SOSBoardCommentRepository sosBoardCommentRepository;

    public Vet registerVet(VetDTO vetDTO) throws IOException {
        // 프로필 사진 저장 로직
        String profilePhotoFileName = null;
        if (vetDTO.getProfilePhoto() != null && !vetDTO.getProfilePhoto().isEmpty()) {
            profilePhotoFileName = fileController.saveProfilePhoto(vetDTO.getProfilePhoto());
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(vetDTO.getPassword()); // 예시로 이름+123으로 기본 비밀번호 설정

        // User 엔티티 생성
        User user = User.builder()
                .uid(vetDTO.getUsername())
                .email(vetDTO.getEmail()) // 이메일
                .nickname(vetDTO.getVetname()) // 닉네임을 수의사 이름으로 설정
                .name(vetDTO.getVetname()) // 이름도 수의사 이름으로 설정
                .password(encodedPassword) // 암호화된 비밀번호
                .profilePhoto(profilePhotoFileName) // 프로필 사진
                .vet(true)
                .build();

        // 저장 (User 먼저 저장)
        User savedUser = userRepository.save(user);

        // Vet 엔티티 생성 (User 엔티티의 ID를 참조)
        Vet vet = Vet.builder()
                .vetname(vetDTO.getVetname())
                .password(encodedPassword) // 암호화된 비밀번호
                .username(vetDTO.getUsername())
                .animalhospitlename(vetDTO.getAnimalhospitlename())
                .registerdate(vetDTO.getRegisterdate())
                .withdrawaldate(vetDTO.getWithdrawaldate() != null ? Timestamp.valueOf(vetDTO.getWithdrawaldate() + " 00:00:00") : null)
                .email(vetDTO.getEmail())
                .profilePhoto(profilePhotoFileName)
                .description(vetDTO.getDescription())
                .visibility(vetDTO.getVisibility())
                .user(savedUser) // User 엔티티와 연관 설정
                .build();

        // Vet 저장
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

    @Transactional
    public void deleteVet(Long vetId) {
        // Vet 엔티티를 먼저 조회
        Vet vet = vetRepository.findById(vetId)
                .orElseThrow(() -> new EntityNotFoundException("해당 수의사를 찾을 수 없습니다."));

        // Vet과 연관된 User 엔티티의 vetinfo 필드를 null로 설정
        User user = vet.getUser();
        if (user != null) {
            user.setVetinfo(null); // vetinfo 필드를 null로 설정
            userRepository.save(user); // User 정보 저장
        }

        // Vet 삭제
        vetRepository.delete(vet);
    }


    public Vet getVetByUserId(Long userId) {
        // 사용자를 Optional로부터 추출
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        // 사용자의 vetinfo 필드 반환
        Vet vet = user.getVetinfo();

        if (vet == null) {
            throw new RuntimeException("사용자와 연결된 수의사를 찾을 수 없습니다.");
        }

        return vet;
    }

    @Transactional
    public SOSboardcomment getCommentById(Long commentId) {
        return sosBoardCommentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));
    }

    @Transactional
    public void recommendVet(Long vetId, Long commentId, Long userId, User currentUser) {
        // 댓글에서 수의사 정보 추출 (commentId 사용)
        SOSboardcomment comment = getCommentById(commentId); // commentId로 댓글 정보 가져오기
        User commentUser = comment.getUser(); // 댓글 작성자 정보
        Vet vet = commentUser.getVetinfo(); // 댓글 작성자가 수의사일 경우 수의사 정보 추출

        if (vet == null) {
            throw new RuntimeException("수의사를 찾을 수 없습니다.");
        }

        // 현재 로그인한 사용자가 이미 추천했는지 확인
        boolean alreadyRecommended = vetRecommendationRepository.existsByUserUidAndVetVetid(currentUser.getUid(), vetId);

        if (alreadyRecommended) {
            throw new RuntimeException("이미 추천한 수의사입니다.");
        }

        // 추천 객체 생성 및 저장
        VetRecommendation recommendation = VetRecommendation.builder()
                .vet(vet) // 수의사 정보
                .user(currentUser) // 추천한 사용자 정보
                .recommendDate(new Date())
                .build();

        vetRecommendationRepository.save(recommendation); // 추천 저장

        // 추천 카운트 증가
        vet.setRecommendationCount(vet.getRecommendationCount() + 1);
        vetRepository.save(vet); // 변경된 추천 카운트 저장
    }

    public List<Vet> getTop3VetsByRecommendation() {
        return vetRepository.findTop3ByOrderByRecommendationCountDesc();
    }


    // 수의사 목록을 가져오면서 추천수와 댓글 수를 함께 가져오는 메서드
    public List<VetDTO> getTopVetsWithCommentCounts() {
        List<Vet> vets = vetRepository.findAll(); // 모든 수의사 조회

        return vets.stream()
                .map(vet -> VetDTO.builder()
                        .vetid(vet.getVetid())
                        .vetname(vet.getVetname())
                        .animalhospitlename(vet.getAnimalhospitlename())
                        .recommendationCount(vet.getRecommendationCount())
                        .commentCount(vet.getCommentCount()) // 댓글 수 계산
                        .profilePhotoPath(vet.getProfilePhoto())
                        .build())
                .collect(Collectors.toList());
    }

}