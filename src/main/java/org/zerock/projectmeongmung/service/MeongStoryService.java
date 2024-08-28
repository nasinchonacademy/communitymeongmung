package org.zerock.projectmeongmung.service;

import org.zerock.projectmeongmung.dto.MeongStoryDTO;
import org.zerock.projectmeongmung.dto.PageRequestDTO;
import org.zerock.projectmeongmung.dto.PageResultDTO;
import org.zerock.projectmeongmung.entity.MeongStory;
import org.zerock.projectmeongmung.entity.User;

public interface MeongStoryService {
    Long register(MeongStoryDTO dto);
    PageResultDTO<MeongStoryDTO, MeongStory> getList(PageRequestDTO requestDTO);
    PageResultDTO<MeongStoryDTO, MeongStory> getPetFriendlyLocations(PageRequestDTO requestDTO);
    PageResultDTO<MeongStoryDTO, MeongStory> getDailyItems(PageRequestDTO requestDTO);
    MeongStoryDTO read(Long seq);
    void modify(MeongStoryDTO dto);
    void remove(Long seq);
    PageResultDTO<MeongStoryDTO, MeongStory> getAllItems(PageRequestDTO requestDTO);

    // DTO -> Entity
    MeongStory dtoToEntity(MeongStoryDTO dto);
    MeongStory dtoToEntity(MeongStoryDTO dto, User user); // 추가된 메서드

    // Entity -> DTO
    MeongStoryDTO entityToDto(MeongStory entity);
}
