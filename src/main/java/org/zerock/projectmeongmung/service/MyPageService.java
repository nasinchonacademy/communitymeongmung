package org.zerock.projectmeongmung.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zerock.projectmeongmung.entity.MeongStory;
import org.zerock.projectmeongmung.entity.StoryLike;
import org.zerock.projectmeongmung.entity.User;
import org.zerock.projectmeongmung.repository.MeongStoryRepository;
import org.zerock.projectmeongmung.repository.StoryLikeRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MyPageService {

    @Autowired
    private StoryLikeRepository storyLikeRepository;
    @Autowired
    private MeongStoryRepository meongStoryRepository;

    public List<MeongStory> getLikedStories(User user) {
        List<StoryLike> storyLikes = storyLikeRepository.findByUser(user);
        return storyLikes.stream()
                .map(StoryLike::getStorySeq)
                .collect(Collectors.toList());
    }

    public List<MeongStory>getWrittenStories(User user){
        return meongStoryRepository.findByUserId(user.getId());
    }

}
