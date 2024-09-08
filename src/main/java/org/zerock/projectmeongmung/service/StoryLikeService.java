package org.zerock.projectmeongmung.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zerock.projectmeongmung.entity.MeongStory;
import org.zerock.projectmeongmung.entity.StoryLike;
import org.zerock.projectmeongmung.entity.User;
import org.zerock.projectmeongmung.repository.MeongStoryRepository;
import org.zerock.projectmeongmung.repository.StoryLikeRepository;

import java.time.LocalDate;

@Service
public class StoryLikeService {

    @Autowired
    private StoryLikeRepository storyLikeRepository;

    @Autowired
    private MeongStoryRepository meongStoryRepository; // 게시물의 좋아요 수를 업데이트할 수 있는 repository

    public boolean hasLikedBefore(User user, MeongStory storySeq) {
        // 사용자가 이전에 해당 게시물에 좋아요를 눌렀는지 확인
        return storyLikeRepository.existsByUserAndStorySeq(user, storySeq);
    }




    public void saveLikeRecord(User user, MeongStory storySeq, LocalDate today) {
        StoryLike storyLike = new StoryLike();
        storyLike.setUser(user);
        storyLike.setStorySeq(storySeq);
        storyLike.setLikeDate(today);
        storyLikeRepository.save(storyLike);
    }

    @Transactional
    public void incrementLikeCount(MeongStory story) {
        meongStoryRepository.incrementLikeCount(story.getSeq());
    }
}
