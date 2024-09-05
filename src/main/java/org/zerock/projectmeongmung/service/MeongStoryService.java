package org.zerock.projectmeongmung.service;

import org.zerock.projectmeongmung.dto.MeongStoryDTO;
import org.zerock.projectmeongmung.dto.PageRequestDTO;
import org.zerock.projectmeongmung.dto.PageResultDTO;
import org.zerock.projectmeongmung.dto.StoryCommentDto;
import org.zerock.projectmeongmung.entity.MeongStory;
import org.zerock.projectmeongmung.entity.User;

public interface MeongStoryService {

    // 등록 메서드
    Long register(MeongStoryDTO dto);


    // 전체 목록 가져오기 (검색 기능 포함)
    PageResultDTO<MeongStoryDTO, MeongStory> getAllItems(PageRequestDTO requestDTO);

    // 전체 리스트 가져오기 (기존 메서드)
    PageResultDTO<MeongStoryDTO, MeongStory> getList(PageRequestDTO requestDTO);

    // 애견 동반 장소 가져오기 (검색 기능 포함)
    PageResultDTO<MeongStoryDTO, MeongStory> getPetFriendlyLocations(PageRequestDTO requestDTO);

    // 일상 목록 가져오기 (검색 기능 포함)
    PageResultDTO<MeongStoryDTO, MeongStory> getDailyItems(PageRequestDTO requestDTO);

    // 특정 글 읽기
    MeongStoryDTO read(Long seq);

    // 글 수정
    void modify(MeongStoryDTO dto);

    // 글 삭제
    void remove(Long seq);

    // DTO -> Entity 변환 메서드
    MeongStory dtoToEntity(MeongStoryDTO dto);
    MeongStory dtoToEntity(MeongStoryDTO dto, User user);

    // Entity -> DTO 변환 메서드
    MeongStoryDTO entityToDto(MeongStory entity);

    int increaseLikeCount(Long seq);

    void incrementViewCount(long seq);

}
