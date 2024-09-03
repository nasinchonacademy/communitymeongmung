package org.zerock.projectmeongmung.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.projectmeongmung.dto.AddUserRequest;
import org.zerock.projectmeongmung.entity.User;
import org.zerock.projectmeongmung.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // 회원 정보 저장
    public Long save(AddUserRequest dto) {
        return userRepository.save(User.builder()
                .uid(dto.getUid())
                .password(bCryptPasswordEncoder.encode(dto.getPassword()))
                .email(dto.getEmail())
                .name(dto.getName())
                .nickname(dto.getNickname())
                .profilePhoto(dto.getProfilePhoto())
                .dogbirthday(dto.getDogbirthday())
                .dogname(dto.getDogname())
                .dogbreed(dto.getDogbreed())
                .dogmeeting(dto.getDogmeeting())
                .termuse(dto.isTermuse())
                .locservice(dto.isLocservice())
                .marketsns(dto.isMarketsns())
                .personalinfo(dto.isPersonalinfo())
                .jellypoint(dto.getJellypoint())
                .build()).getId();

    }

    // UID 중복확인
    public boolean checkDuplicateUid(String uid) {
        return userRepository.findByUid(uid).isPresent();
    }

    // 닉네임 중복확인
    public boolean checkDuplicateNickname(String nickname) {
        return userRepository.findByNickname(nickname).isPresent();
    }

    // 회원정보 업데이트
    public void updateUserInfo(User existingUser, User updatedUserData) {
        // 기존 사용자 정보를 새로운 데이터로 업데이트
        User updatedUser = User.builder()
                .uid(existingUser.getUid())  // UID는 변경되지 않도록 기존 값을 유지
                .nickname(updatedUserData.getNickname())
                .dogname(updatedUserData.getDogname())
                .profilePhoto(updatedUserData.getProfilePhoto())
                .dogbirthday(updatedUserData.getDogbirthday())
                .dogbreed(updatedUserData.getDogbreed())
                .build();

        userRepository.save(updatedUser);

    }

    // uid로 사용자 정보 조회
    public User findByUid(String uid) {
        return userRepository.findByUid(uid)
                .orElseThrow(() -> new IllegalArgumentException(uid + "님의 정보를 찾을 수 없습니다."));
    }

    // 게임 점수 젤리 포인트 연계
    // 젤리 포인트 추가
    @Transactional
    public void addJellyPointsToUser(String uid, int points) {
        User user = findByUid(uid);
        user.addJellyPoints(points);
        userRepository.save(user);
    }

    // 젤리 포인트 차감
    public void subtractJellyPointsFromUser(String uid, int points) {
        User user = findByUid(uid);
        user.subtractJellyPoints(points);  // User 엔티티의 subtractJellyPoints 메서드 호출
        userRepository.save(user);
    }

}
