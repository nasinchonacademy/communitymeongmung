package org.zerock.projectmeongmung.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.zerock.projectmeongmung.dto.MeongStoryDTO;
import org.zerock.projectmeongmung.dto.PageRequestDTO;
import org.zerock.projectmeongmung.dto.PageResultDTO;
import org.zerock.projectmeongmung.entity.Cart;
import org.zerock.projectmeongmung.entity.MeongStory;
import org.zerock.projectmeongmung.entity.StoryLike;
import org.zerock.projectmeongmung.entity.User;
import org.zerock.projectmeongmung.repository.CartRepository;
import org.zerock.projectmeongmung.repository.MeongStoryRepository;
import org.zerock.projectmeongmung.repository.StoryLikeRepository;

import java.util.List;
import java.util.function.Function;
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

    public PageResultDTO<MeongStoryDTO, MeongStory> getLikedStories2(User user, PageRequestDTO pageRequestDTO) {
        // 사용자가 좋아요한 게시물만 필터링
        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize());

        String keyword = pageRequestDTO.getKeyword() == null ? "" : pageRequestDTO.getKeyword();

        // 좋아요한 게시물 목록을 가져오는 쿼리 실행
        Page<MeongStory> likedStories = meongStoryRepository.findByLikesUser(user, keyword,pageable);

        // DTO로 변환하여 반환
        Function<MeongStory, MeongStoryDTO> fn = (entity -> MeongStoryDTO.fromEntity(entity));
        return new PageResultDTO<>(likedStories, fn);
    }

    public PageResultDTO<MeongStoryDTO, MeongStory> getWrittenStories(User user, PageRequestDTO pageRequestDTO) {
        Pageable pageable = PageRequest.of(pageRequestDTO.getPage() - 1, pageRequestDTO.getSize());

        // 키워드를 기준으로 검색 가능, 키워드가 없을 경우 빈 문자열로 처리
        String keyword = pageRequestDTO.getKeyword() == null ? "" : pageRequestDTO.getKeyword();

        // 사용자와 키워드에 맞는 게시물 조회
        Page<MeongStory> writtenStories = meongStoryRepository.findByUserAndTitleContaining(user, keyword, pageable);

        // 엔티티를 DTO로 변환
        Function<MeongStory, MeongStoryDTO> fn = (entity -> MeongStoryDTO.fromEntity(entity));

        return new PageResultDTO<>(writtenStories, fn);
    }

}
