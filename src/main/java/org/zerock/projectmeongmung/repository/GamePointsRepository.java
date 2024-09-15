package org.zerock.projectmeongmung.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.projectmeongmung.entity.GamePoints;
import org.zerock.projectmeongmung.entity.User;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface GamePointsRepository extends JpaRepository<GamePoints, Long> {
    List<GamePoints> findByUser(User user);

    Optional<GamePoints> findTopByUserAndGameTypeOrderByTimePlayedDesc(User user, String gameType);

    @Query("SELECT DISTINCT gp.gameType FROM GamePoints gp WHERE gp.user.id = :userId AND FUNCTION('DATE', gp.timePlayed) = :date")
    List<String> findGameTypesByUserIdAndDate(@Param("userId") Long userId, @Param("date") LocalDate date);
    int countByUserAndGameTypeAndTimePlayedBetween(User user, String gameType, Timestamp startTime, Timestamp endTime);


}

