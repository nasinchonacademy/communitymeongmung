package org.zerock.projectmeongmung.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zerock.projectmeongmung.entity.*;
import org.zerock.projectmeongmung.repository.SOSBoardLikeRepository;
import org.zerock.projectmeongmung.repository.SOSboardRepository;

import java.time.LocalDate;

@Service
public class SOSlilkeService {

    @Autowired
    private SOSBoardLikeRepository boardLikeRepository;
    @Autowired
    private SOSboardRepository boardRepository;


    public boolean checkIfUserLiked(User user, SOSboard sosboard, LocalDate likeDate) {
        return boardLikeRepository.existsByMemberAndSosboardAndLikecountupdate(user, sosboard, likeDate);
    }

    public void saveLikeRecord(User user, SOSboard sosboardseq, LocalDate today) {
        SOSboardlikecount boardlike = new SOSboardlikecount();
        boardlike.setMember(user);
        boardlike.setSosboard(sosboardseq);
        boardlike.setLikecountupdate(today);
        boardLikeRepository.save(boardlike);
    }

    @Transactional
    public void incrementLikeCount(SOSboard sosboard) {
        boardRepository.incrementLikeCount(sosboard.getSosboardseq());
    }

}
