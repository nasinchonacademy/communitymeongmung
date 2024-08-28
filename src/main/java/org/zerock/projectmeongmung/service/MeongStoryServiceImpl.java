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

import java.util.function.Function;

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
        Pageable pageable = requestDTO.getPageable(Sort.by("seq").descending());
        Page<MeongStory> result = meongStoryRepository.findByCategory("Category1", pageable);
        Function<MeongStory, MeongStoryDTO> fn = this::entityToDto;
        return new PageResultDTO<>(result, fn);
    }

    @Override
    public PageResultDTO<MeongStoryDTO, MeongStory> getDailyItems(PageRequestDTO requestDTO) {
        Pageable pageable = requestDTO.getPageable(Sort.by("seq").descending());
        Page<MeongStory> result = meongStoryRepository.findByCategory("Category2", pageable);
        Function<MeongStory, MeongStoryDTO> fn = this::entityToDto;
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
        Page<MeongStory> result = meongStoryRepository.findAll(pageable);
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
}
