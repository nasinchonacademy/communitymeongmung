package org.zerock.projectmeongmung.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.projectmeongmung.dto.AddUserRequest;
import org.zerock.projectmeongmung.entity.GamePoints;
import org.zerock.projectmeongmung.entity.User;
import org.zerock.projectmeongmung.repository.GamePointsRepository;
import org.zerock.projectmeongmung.repository.UserRepository;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final GamePointsRepository gamePointsRepository;  // GamePointsRepository 주입
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
    @Transactional
    public void updateUserInfo(User existingUser, User updatedUserData) {
        // 기존 사용자 정보를 새로운 데이터로 업데이트
//        User updatedUser = User.builder()
//                .uid(existingUser.getUid())  // UID는 변경되지 않도록 기존 값을 유지
//                .nickname(updatedUserData.getNickname())
//                .dogname(updatedUserData.getDogname())
//                .profilePhoto(updatedUserData.getProfilePhoto())
//                .dogbirthday(updatedUserData.getDogbirthday())
//                .dogbreed(updatedUserData.getDogbreed())
//                .build();

        existingUser.setNickname(updatedUserData.getNickname());
        existingUser.setDogname(updatedUserData.getDogname());
        existingUser.setProfilePhoto(updatedUserData.getProfilePhoto());
        existingUser.setDogbirthday(updatedUserData.getDogbirthday());
        existingUser.setDogbreed(updatedUserData.getDogbreed());

        // userRepository.save(updatedUser);

        // 업데이트된 사용자 정보 저장
        userRepository.save(existingUser);

    }

    // 이메일을 통해 User 엔티티 조회
    public User findByEmail(String email) {
        Optional<User> userOptional = userRepository.findByEmail(email);
        return userOptional.orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
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
    @Transactional
    public void subtractJellyPointsFromUser(String uid, int points) {
        User user = findByUid(uid);
        user.subtractJellyPoints(points);  // User 엔티티의 subtractJellyPoints 메서드 호출
        userRepository.save(user);
    }

    // 사용자 정보 저장 메서드 추가
    public User save(User user) {
        return userRepository.save(user); // 사용자 정보 저장
    }

    // 사용자가 오늘 게임을 했는지 확인
    public boolean hasPlayedToday(String uid, String gameType) {
        User user = findByUid(uid);
        Optional<GamePoints> lastGameOpt = gamePointsRepository.findTopByUserAndGameTypeOrderByTimePlayedDesc(user, gameType);

        if (lastGameOpt.isPresent()) {
            GamePoints lastGame = lastGameOpt.get();
            LocalDate lastPlayedDate = lastGame.getTimePlayed().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate today = LocalDate.now();

            return lastPlayedDate.isEqual(today); // 마지막으로 플레이한 날짜가 오늘인지 확인
        }

        return false; // 게임 기록이 없으면 오늘 게임을 하지 않은 것으로 간주

    }

    // 추가: 게임 후 게임 기록을 업데이트하는 메서드
    @Transactional
    public void updateGamePoints(String uid, String gameType, int points, int restCount) {
        User user = findByUid(uid);
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());

        // 젤리 포인트 추가
        user.addJellyPoints(points);

        GamePoints gamePoints = GamePoints.builder()
                .user(user)
                .gameType(gameType)
                .point(points)
                .timePlayed(currentTime)
                .restCount(restCount)
                .build();

        gamePointsRepository.save(gamePoints); // 새로운 게임 기록 저장
        userRepository.save(user); // 사용자 업데이트 (포인트 변경 사항 저장)
    }

    public User findUserByUid(String username) {
        return userRepository.findByUid(username).orElseThrow(() -> new RuntimeException("User not found"));
    }
}
