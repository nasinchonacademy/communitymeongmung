package org.zerock.projectmeongmung.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zerock.projectmeongmung.entity.GamePoints;
import org.zerock.projectmeongmung.repository.GamePointsRepository;
import org.zerock.projectmeongmung.repository.UserRepository;
import org.zerock.projectmeongmung.entity.User;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;



@Service
@RequiredArgsConstructor
public class gameService {

    private final UserRepository UserRepository;
    private final GamePointsRepository GamePointsRepository;

    public List<GamePoints> getGamePoints(User user) {
        return GamePointsRepository.findByUser(user);
    }



    // 사용자가 오늘 플레이한 게임 타입 목록을 가져옴 (포인트 값에 관계없이)
    public List<String> getPlayedGamesToday(Long userId, LocalDate date) {
        return GamePointsRepository.findGameTypesByUserIdAndDate(userId, date);
    }

    public boolean hasPlayedToday(String uid, String gameType) {
        User user = UserRepository.findByUid(uid)
                .orElseThrow(() -> new RuntimeException("User not found with UID: " + uid));

        LocalDate today = LocalDate.now();

        // 오늘 날짜의 시작과 끝 시간 계산
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);

        // 오늘 해당 게임을 이미 했는지 확인
        int count = GamePointsRepository.countByUserAndGameTypeAndTimePlayedBetween(
                user, gameType, Timestamp.valueOf(startOfDay), Timestamp.valueOf(endOfDay));

        return count > 0;
    }

    public void updateGamePoints(String uid, String gameType, int points, int jellyCount) {
        User user = UserRepository.findByUid(uid)
                .orElseThrow(() -> new RuntimeException("User not found with UID: " + uid));

        // 새로운 게임 포인트 기록 추가
        GamePoints gamePoints = GamePoints.builder()
                .user(user)
                .gameType(gameType)
                .point(points)
                .timePlayed(Timestamp.valueOf(LocalDateTime.now())) // 현재 시간으로 기록
                .restCount(jellyCount)
                .build();

        GamePointsRepository.save(gamePoints);
    }



}
