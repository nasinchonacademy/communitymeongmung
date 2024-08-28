package org.zerock.projectmeongmung.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zerock.projectmeongmung.entity.GamePoints;
import org.zerock.projectmeongmung.repository.GamePointsRepository;
import org.zerock.projectmeongmung.repository.UserRepository;
import org.zerock.projectmeongmung.entity.User;
import java.util.List;



@Service
@RequiredArgsConstructor
public class gameService {

    private final UserRepository UserRepository;
    private final GamePointsRepository GamePointsRepository;

    public List<GamePoints> getGamePoints(User user) {
        return GamePointsRepository.findByUser(user);
    }



}
