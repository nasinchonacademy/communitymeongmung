package org.zerock.projectmeongmung.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.zerock.projectmeongmung.dto.StoryCommentDto;
import org.zerock.projectmeongmung.entity.StoryComment;
import org.zerock.projectmeongmung.repository.MeongStoryRepository;
import org.zerock.projectmeongmung.repository.StoryCommentRepository;
import org.zerock.projectmeongmung.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MeongStoryCommentServiceImpl implements MeongStoryCommentService {

    private final MeongStoryRepository meongStoryRepository;
    private final StoryCommentRepository storyCommentRepository;
    private final UserRepository userRepository;

    @Override
    public StoryCommentDto Commentread(Long seq) {
        // 특정 댓글을 가져와서 DTO로 변환
        return storyCommentRepository.findById(seq)
                .map(this::entityToDto)
                .orElse(null);
    }

    public List<StoryCommentDto> getCommentsByStorySeq(Long seq) {
        // 특정 게시글에 속한 모든 댓글을 가져와서 DTO로 변환
        List<StoryComment> comments = storyCommentRepository.findByStorySeq(seq);
        return comments.stream()
                .map(this::entityToDto)  // 엔티티를 DTO로 변환
                .collect(Collectors.toList());
    }

    @Override
    public void CommentRegister(StoryCommentDto commentDto) {
        // 댓글 등록 로직
        StoryComment storyComment = StoryComment.builder()
                .commentcontent(commentDto.getCommentcontent())
                .user(userRepository.findById(commentDto.getId())
                        .orElseThrow(() -> new RuntimeException("User not found")))
                .story(meongStoryRepository.findById(commentDto.getSeq())
                        .orElseThrow(() -> new RuntimeException("Story not found")))
                .build();

        storyCommentRepository.save(storyComment);
    }

    // 엔티티를 DTO로 변환하는 메서드
    private StoryCommentDto entityToDto(StoryComment storyComment) {
        return StoryCommentDto.builder()
                .commentid(storyComment.getCommentid())
                .id(storyComment.getUser().getId())  // User의 ID
                .nickname(storyComment.getUser().getNickname()) // User의 닉네임
                .seq(storyComment.getStory().getSeq())  // Story의 seq
                .commentcontent(storyComment.getCommentcontent())
                .build();
    }

    @Override
    public void removeComment(Long commentId) {
        storyCommentRepository.deleteById(commentId);  // 댓글 ID로 댓글 삭제
    }

}