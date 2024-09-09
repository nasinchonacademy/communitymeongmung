package org.zerock.projectmeongmung.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zerock.projectmeongmung.entity.Cart;
import org.zerock.projectmeongmung.entity.MeongStory;
import org.zerock.projectmeongmung.entity.StoryLike;
import org.zerock.projectmeongmung.entity.User;
import org.zerock.projectmeongmung.repository.CartRepository;
import org.zerock.projectmeongmung.repository.MeongStoryRepository;
import org.zerock.projectmeongmung.repository.StoryLikeRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final StoryLikeRepository storyLikeRepository;
    private  final MeongStoryRepository meongStoryRepository;
    private final CartRepository cartRepository;

    public List<MeongStory> getLikedStories(User user) {
        List<StoryLike> storyLikes = storyLikeRepository.findByUser(user);
        return storyLikes.stream()
                .map(StoryLike::getStorySeq)
                .collect(Collectors.toList());
    }

    public List<MeongStory>getWrittenStories(User user){
        return meongStoryRepository.findByUserId(user.getId());
    }

    // 장바구니 리스트 가져오기
    public List<Cart> getCartItems(User user) {
        return cartRepository.findByUser(user);
    }

}
