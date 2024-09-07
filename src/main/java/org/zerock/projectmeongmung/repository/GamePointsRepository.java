package org.zerock.projectmeongmung.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zerock.projectmeongmung.entity.GamePoints;
import org.zerock.projectmeongmung.entity.User;

import java.util.List;
import java.util.Optional;

public interface GamePointsRepository extends JpaRepository<GamePoints, Long> {
    List<GamePoints> findByUser(User user);

    Optional<GamePoints> findTopByUserAndGameTypeOrderByTimePlayedDesc(User user, String gameType);
}
