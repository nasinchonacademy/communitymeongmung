package org.zerock.projectmeongmung.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.zerock.projectmeongmung.dto.MeongStoryDTO;
import org.zerock.projectmeongmung.dto.PageRequestDTO;
import org.zerock.projectmeongmung.dto.PageResultDTO;
import org.zerock.projectmeongmung.entity.MeongStory;
import org.zerock.projectmeongmung.entity.User;
import org.zerock.projectmeongmung.repository.MeongStoryRepository;
import org.zerock.projectmeongmung.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class MeongStoryServiceImpl implements MeongStoryService {

    private final MeongStoryRepository meongStoryRepository;
    private final UserRepository userRepository;

    @Override
    public Long register(MeongStoryDTO dto) {
        User user = userRepository.findByUid(dto.getUid())
                .orElseThrow(() -> new IllegalArgumentException("User not found with uid: " + dto.getUid()));

        MeongStory entity = dtoToEntity(dto, user);
        meongStoryRepository.save(entity);

        return entity.getSeq();
    }

    @Override
    public PageResultDTO<MeongStoryDTO, MeongStory> getList(PageRequestDTO requestDTO) {
        Pageable pageable = requestDTO.getPageable(Sort.by("seq").descending());
        Page<MeongStory> result = meongStoryRepository.findAll(pageable);
        Function<MeongStory, MeongStoryDTO> fn = this::entityToDto;
        return new PageResultDTO<>(result, fn);
    }

    @Override
    public PageResultDTO<MeongStoryDTO, MeongStory> getPetFriendlyLocations(PageRequestDTO requestDTO) {

        String keyword = requestDTO.getKeyword();
        boolean isSubmitted = requestDTO.isSubmitted();

        Pageable pageable = requestDTO.getPageable(Sort.by("seq").descending());

        // 'Category1'에 해당하는 결과를 검색
        Page<MeongStory> result = meongStoryRepository.findByCategory("Category1", pageable);
        // 키워드가 존재하거나, submit이 발생한 경우 추가 검색 수행

        if (isSubmitted && (keyword == null || keyword.isEmpty())) {
            // 키워드가 없는 경우 기본 검색을 유지
            result = meongStoryRepository.findByCategory("Category1", pageable);
        } else if (keyword != null && !keyword.isEmpty()) {
            // 키워드가 있는 경우, 키워드로 검색 수행
            result = meongStoryRepository.searchPetFriendlyLocationsByKeyword(keyword, pageable);
        }

        // 엔티티를 DTO로 변환하는 함수
        Function<MeongStory, MeongStoryDTO> fn = this::entityToDto;

        // PageResultDTO로 결과 반환
        return new PageResultDTO<>(result, fn);
    }


    @Override
    public PageResultDTO<MeongStoryDTO, MeongStory> getDailyItems(PageRequestDTO requestDTO) {

        String keyword = requestDTO.getKeyword();
        boolean isSubmitted = requestDTO.isSubmitted();

        Pageable pageable = requestDTO.getPageable(Sort.by("seq").descending());

        // 검색 결과를 담을 변수 선언
        Page<MeongStory> result = meongStoryRepository.findByCategory("Category2", pageable);

        // 키워드가 존재하거나, submit이 발생한 경우 추가 검색 수행
        if (isSubmitted && (keyword == null || keyword.isEmpty())) {
            // 키워드가 없는 경우 기본 검색을 유지
            result = meongStoryRepository.findByCategory("Category2", pageable);
        } else if (keyword != null && !keyword.isEmpty()) {
            // 키워드가 있는 경우, 키워드로 검색 수행
            result = meongStoryRepository.searchDailyItemsByKeyword(keyword, pageable);
        }

        // 엔티티를 DTO로 변환하는 함수 적용
        Function<MeongStory, MeongStoryDTO> fn = this::entityToDto;

        // 결과를 PageResultDTO로 반환
        return new PageResultDTO<>(result, fn);
    }



    @Override
    public MeongStoryDTO read(Long seq) {
        return meongStoryRepository.findById(seq).map(this::entityToDto).orElse(null);
    }

    @Override
    public void modify(MeongStoryDTO dto) {
        MeongStory entity = meongStoryRepository.findById(dto.getSeq())
                .orElseThrow(() -> new IllegalArgumentException("Story not found with seq: " + dto.getSeq()));

        entity.setTitle(dto.getTitle());
        entity.setContent(dto.getContent());
        entity.setLikecount(dto.getLikecount());
        entity.setPicture(dto.getPicture());
        entity.setViewcount(dto.getViewcount());
        entity.setCategory(dto.getCategory());

        meongStoryRepository.save(entity);
    }

    @Override
    public void remove(Long seq) {
        meongStoryRepository.deleteById(seq);
    }

    // 추가된 메서드
    @Override
    public MeongStory dtoToEntity(MeongStoryDTO dto) {
        User user = userRepository.findByUid(dto.getUid())
                .orElseThrow(() -> new IllegalArgumentException("User not found with uid: " + dto.getUid()));

        return MeongStory.builder()
                .seq(dto.getSeq())
                .title(dto.getTitle())
                .content(dto.getContent())
                .likecount(dto.getLikecount())
                .picture(dto.getPicture())
                .viewcount(dto.getViewcount())
                .category(dto.getCategory())
                .deleted(dto.getDeleted())
                .user(user)
                .build();
    }

    @Override
    public MeongStory dtoToEntity(MeongStoryDTO dto, User user) {
        return MeongStory.builder()
                .seq(dto.getSeq())
                .title(dto.getTitle())
                .content(dto.getContent())
                .likecount(dto.getLikecount())
                .picture(dto.getPicture())
                .viewcount(dto.getViewcount())
                .category(dto.getCategory())
                .deleted(dto.getDeleted())
                .user(user)
                .build();
    }

    @Override
    public PageResultDTO<MeongStoryDTO, MeongStory> getAllItems(PageRequestDTO requestDTO) {
        Pageable pageable = requestDTO.getPageable(Sort.by("seq").descending());
        Page<MeongStory> result;

        String keyword = requestDTO.getKeyword();
        String category = requestDTO.getCategory();
        String type = requestDTO.getType();

        if (category != null && !category.isEmpty()) {
            // 카테고리가 설정된 경우 카테고리와 키워드로 검색
            log.info("Searching in Category {} with keyword: {}", category, keyword);
            result = meongStoryRepository.searchByKeywordAndCategory(keyword, category, pageable);
        } else if (type != null && "tcw".equals(type) && keyword != null && !keyword.isEmpty()) {
            // 카테고리가 없고 키워드로만 검색하는 경우
            log.info("Executing search by keyword: {}", keyword);
            result = meongStoryRepository.searchByKeyword(keyword, pageable);
        } else {
            // 모든 항목 검색
            log.info("Executing findAll");
            result = meongStoryRepository.findAll(pageable);
        }

        Function<MeongStory, MeongStoryDTO> fn = this::entityToDto;
        return new PageResultDTO<>(result, fn);
    }

    @Override
    public MeongStoryDTO entityToDto(MeongStory entity) {
        return MeongStoryDTO.builder()
                .seq(entity.getSeq())
                .title(entity.getTitle())
                .content(entity.getContent())
                .likecount(entity.getLikecount())
                .picture(entity.getPicture())
                .viewcount(entity.getViewcount())
                .category(entity.getCategory())
                .regdate(entity.getRegdate())
                .modified(entity.getModified())
                .deleted(entity.getDeleted())
                .uid(entity.getUser().getUid())
                .nickname(entity.getUser().getNickname())  // User의 nickname을 DTO에 설정
                .build();
    }

    @Override
    public int increaseLikeCount(Long seq) {
        Optional<MeongStory> result = meongStoryRepository.findById(seq); // 인스턴스 변수로 호출

        if (result.isPresent()) {
            MeongStory story = result.get();
            story.setLikecount(story.getLikecount() + 1); // 좋아요 수 증가
            meongStoryRepository.save(story); // 변경 사항 저장
            return story.getLikecount(); // 증가된 좋아요 수 반환
        } else {
            throw new IllegalArgumentException("존재하지 않는 게시물입니다.");
        }
    }

    @Override
    public void incrementViewCount(long seq) {
        MeongStory story = meongStoryRepository.findById(seq)
                .orElseThrow(() -> new RuntimeException("Story not found with seq: " + seq));
        story.setViewcount(story.getViewcount() + 1); // viewcount 증가
        meongStoryRepository.save(story); // 업데이트
    }

    @Override
    public List<MeongStoryDTO> getTop5StoriesByLikeCount() {
        List<MeongStory> topStories = meongStoryRepository.findTop5ByOrderByLikecountDesc();
        return topStories.stream().map(this::entityToDto).collect(Collectors.toList());
    }

}