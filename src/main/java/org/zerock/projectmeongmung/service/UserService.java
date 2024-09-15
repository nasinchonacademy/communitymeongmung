package org.zerock.projectmeongmung.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final GamePointsRepository gamePointsRepository;  // GamePointsRepository 주입
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private JavaMailSender mailSender;

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
//    public User findByEmail(String email) {
//        Optional<User> userOptional = userRepository.findByEmail(email);
//        return userOptional.orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
//    }

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
    public void updateGamePoints(String uid, String gameType, int points, int jellyCount) {
        // Optional을 통해 유저를 조회하고 없으면 예외를 던짐
        User user = userRepository.findByUid(uid)
                .orElseThrow(() -> new RuntimeException("User not found with UID: " + uid));

        // 새로운 게임 포인트 기록 추가 (0점이어도 기록)
        GamePoints gamePoints = GamePoints.builder()
                .user(user)
                .gameType(gameType)
                .point(points) // 0점이어도 기록
                .timePlayed(Timestamp.valueOf(LocalDateTime.now())) // 현재 시간으로 기록
                .restCount(jellyCount)
                .build();

        gamePointsRepository.save(gamePoints);
    }

    /**
     * 이메일을 통해 유저 정보를 찾습니다.
     * @param email
     * @return Optional<User>
     */
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * 유저의 아이디를 이메일로 전송합니다.
     * @param email
     * @param userId
     */
    public void sendIdToEmail(String email, String userId) {
        String subject = "아이디 찾기 안내";
        String text = "안녕하세요, 요청하신 아이디는 다음과 같습니다: " + userId;

        sendEmail(email, subject, text);
    }

    /**
     * 비밀번호 재설정 링크를 이메일로 전송합니다.
     * @param email
     */
    // 이름과 이메일을 통해 사용자 확인 후 비밀번호 재설정 링크 전송
    public void sendPasswordResetLink(String name, String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            User u = user.get();
            if (u.getName().equals(name)) {
                String resetToken = UUID.randomUUID().toString();
                String subject = "비밀번호 재설정 안내";
                String text = "비밀번호를 재설정하시려면 다음 링크를 클릭해주세요: " +
                        "http://localhost:9990/reset-password?token=" + resetToken;

                sendEmail(email, subject, text);

                u.setResetToken(resetToken);
                userRepository.save(u);
            } else {
                throw new IllegalArgumentException("이름이 일치하지 않습니다.");
            }
        } else {
            throw new IllegalArgumentException("해당 이메일로 가입된 사용자가 없습니다.");
        }
    }


    /**
     * 비밀번호 재설정 로직
     * @param token
     * @param newPassword
     */
    // 비밀번호 재설정 로직
    public void resetPassword(String token, String newPassword) {
        Optional<User> user = userRepository.findByResetToken(token);
        if (user.isPresent()) {
            User u = user.get();
            u.setPassword(bCryptPasswordEncoder.encode(newPassword));  // 비밀번호는 BCrypt로 암호화
            u.setResetToken(null); // 토큰을 비움
            userRepository.save(u);
        }
    }

    /**
     * 이메일 전송 로직
     * @param to
     * @param subject
     * @param text
     */
    private void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
    }

    // 이메일로 uid 찾기
    public String findUserUidByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(User::getUid)  // User 객체에서 uid를 추출
                .orElseThrow(() -> new RuntimeException("해당 이메일로 가입된 사용자가 없습니다."));
    }

    // 인증코드 생성 및 전송
    public String sendVerificationCode(String email) {
        String verificationCode = UUID.randomUUID().toString().substring(0, 6); // 6자리 코드 생성
        String subject = "본인 인증 코드";
        String text = "인증 코드: " + verificationCode;

        sendEmail(email, subject, text);

        return verificationCode; // 인증 코드를 반환하여 세션 등에 저장 가능
    }

    // 비밀번호 재설정 시 본인 인증 처리
    public boolean verifyCode(String enteredCode, String actualCode) {
        return enteredCode.equals(actualCode); // 입력한 코드와 실제 발송된 코드 비교
    }

}
