package org.zerock.projectmeongmung.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.zerock.projectmeongmung.dto.PageRequestDTO;
import org.zerock.projectmeongmung.dto.PageResultDTO;
import org.zerock.projectmeongmung.dto.SOSboardDTO;
import org.zerock.projectmeongmung.entity.SOSboard;
import org.zerock.projectmeongmung.entity.User;
import org.zerock.projectmeongmung.repository.SOSBoardLikeRepository;
import org.zerock.projectmeongmung.repository.SOSboardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.projectmeongmung.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
public class SOSboardService {

    @Autowired
    private SOSboardRepository repository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private SOSBoardLikeRepository sosBoardLikeRepository;

    public Optional<SOSboardDTO> getSOSboardById(Long id) {
        return repository.findById(id)
                .map(this::entityToDTO);
    }

    public Long saveSOSboard(SOSboardDTO dto ) {
        User user = userRepository.findByUid(dto.getUid())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        SOSboard sosboard = DTOToEntity(dto,user);
        repository.save(sosboard);

        return sosboard.getSosboardseq();
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

    private SOSboard DTOToEntity(SOSboardDTO dto,User user) {
        return SOSboard.builder()
                .sosboardseq(dto.getSosboardseq())
                .title(dto.getTitle())
                .content(dto.getContent())
                .regdate(dto.getRegdate())
                .moddate(dto.getModdate())
                .picture(dto.getPicture())
                .commentcount(dto.getCommentcount())
                .viewcount(dto.getViewcount())
                .deldate(dto.getDeldate())
                .likecount(dto.getLikecount())
                .user(user)
                .build();
    }


    public PageResultDTO<SOSboardDTO, SOSboard> searchByKeyword(PageRequestDTO pageRequestDTO, String keyword) {
        Pageable pageable = pageRequestDTO.getPageable(Sort.by("regdate").descending());
        Page<SOSboard> result = repository.findByTitleContainingOrContentContaining(keyword, keyword, pageable);

        Function<SOSboard, SOSboardDTO> fn = (entity -> entityToDTO(entity));

        return new PageResultDTO<>(result, fn);
    }

    public List<SOSboardDTO> getTop3BylikeCount() {
        Pageable pageable = PageRequest.of(0, 3, Sort.by(Sort.Direction.DESC, "likecount"));
        List<SOSboard> top3Boards = repository.findTop3ByOrderByLikecountDesc(pageable);
        return top3Boards.stream().map(this::entityToDTO).collect(Collectors.toList());
    }

    public SOSboardDTO read(Long sosboardseq) {
        return repository.findById(sosboardseq).map(this::entityToDTO).orElse(null);
    }

    public void increaseViewcount(Long sosboardseq) {
        SOSboard soSboard = repository.findById(sosboardseq)
                .orElseThrow(() -> new RuntimeException("Sosboard not found with seq "+ sosboardseq));
        soSboard.setViewcount(soSboard.getViewcount() + 1);
        repository.save(soSboard);
    }

    public int increaseLikeCount(Long sosboardseq) {
        Optional<SOSboard> result = repository.findById(sosboardseq);

        if(result.isPresent()) {
            SOSboard soSboard = result.get();
            soSboard.setLikecount(soSboard.getLikecount() + 1);
            repository.save(soSboard);
            return soSboard.getLikecount();
        }else {
            throw new IllegalArgumentException("존재하지 않는 게시물입니다.");
        }

    }

    public void remove(Long sosboardseq) {
        SOSboard sosboard = repository.findById(sosboardseq)
                .orElseThrow(() -> new IllegalArgumentException("Board not found: " + sosboardseq));

        // 좋아요와 관련된 항목 삭제
        sosBoardLikeRepository.deleteBySosboard(sosboard);
        repository.deleteById(sosboardseq);  // 게시물 삭제
    }

    public void modify( SOSboardDTO dto) {
        SOSboard entity = repository.findById(dto.getSosboardseq())
                .orElseThrow(() -> new IllegalArgumentException("Sosboard not found with seq "+ dto.getSosboardseq()));

        entity.setTitle(dto.getTitle());
        entity.setContent(dto.getContent());
        entity.setRegdate(dto.getRegdate());
        entity.setModdate(dto.getModdate());
        entity.setPicture(dto.getPicture());
        entity.setCommentcount(dto.getCommentcount());
        entity.setViewcount(dto.getViewcount());
        entity.setLikecount(dto.getLikecount());
        entity.setViewcount(dto.getViewcount());

        repository.save(entity);
    }

}