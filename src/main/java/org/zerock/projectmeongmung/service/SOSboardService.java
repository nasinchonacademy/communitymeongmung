package org.zerock.projectmeongmung.service;

import org.springframework.data.domain.Sort;
import org.zerock.projectmeongmung.dto.PageRequestDTO;
import org.zerock.projectmeongmung.dto.PageResultDTO;
import org.zerock.projectmeongmung.dto.SOSboardDTO;
import org.zerock.projectmeongmung.entity.SOSboard;
import org.zerock.projectmeongmung.repository.SOSboardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.function.Function;

@Service
@Transactional
public class SOSboardService {

    @Autowired
    private SOSboardRepository repository;

    public PageResultDTO<SOSboardDTO, SOSboard> getAllSOSboards(PageRequestDTO pageRequestDTO) {
        Pageable pageable = pageRequestDTO.getPageable();

        Page<SOSboard> result;

        if (pageRequestDTO.getKeyword() != null && !pageRequestDTO.getKeyword().isEmpty()) {
            // 검색 조건에 맞는 결과를 페이징 처리하여 가져옴
            result = repository.findByTitleContainingOrContentContaining(
                    pageRequestDTO.getKeyword(),
                    pageRequestDTO.getKeyword(),
                    pageable
            );
        } else {
            // 모든 결과를 페이징 처리하여 가져옴
            result = repository.findAll(pageable);
        }

        Function<SOSboard, SOSboardDTO> fn = (entity -> entityToDTO(entity));

        return new PageResultDTO<>(result, fn);
    }

    public Optional<SOSboardDTO> getSOSboardById(Long id) {
        return repository.findById(id)
                .map(this::entityToDTO);
    }

    public SOSboardDTO saveSOSboard(SOSboard sosboard) {
        SOSboard savedSOSboard = repository.save(sosboard);
        return entityToDTO(savedSOSboard);
    }

    public void deleteSOSboard(Long id) {
        repository.deleteById(id);
    }

    private SOSboardDTO entityToDTO(SOSboard sosboard) {
        return SOSboardDTO.builder()
                .sosboardseq(sosboard.getSosboardseq())
                .title(sosboard.getTitle())
                .content(sosboard.getContent())
                .regdate(sosboard.getRegdate())
                .moddate(sosboard.getModdate())
                .picture(sosboard.getPicture())
                .commentcount(sosboard.getCommentcount())
                .viewcount(sosboard.getViewcount())
                .deldate(sosboard.getDeldate())
                .nickname(sosboard.getUser().getNickname())
                .likecount(sosboard.getLikecount())
                .build();
    }

    public PageResultDTO<SOSboardDTO, SOSboard> getAll(PageRequestDTO pageRequestDTO) {
        Pageable pageable = pageRequestDTO.getPageable(Sort.by("regdate").descending());
        Page<SOSboard> result = repository.findAll(pageable);

        Function<SOSboard, SOSboardDTO> fn = (entity ->  entityToDTO(entity));

        return new PageResultDTO<>(result, fn);
    }

    public PageResultDTO<SOSboardDTO, SOSboard> searchByKeyword(PageRequestDTO pageRequestDTO, String keyword) {
        Pageable pageable = pageRequestDTO.getPageable(Sort.by("regdate").descending());
        Page<SOSboard> result = repository.findByTitleContainingOrContentContaining(keyword, keyword, pageable);

        Function<SOSboard, SOSboardDTO> fn = (entity -> entityToDTO(entity));

        return new PageResultDTO<>(result, fn);
    }
}