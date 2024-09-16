package org.zerock.projectmeongmung.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.projectmeongmung.dto.StoryCommentDto;
import org.zerock.projectmeongmung.entity.*;
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
                .user(userRepository.findById(commentDto.getUserId())
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
                .userId(storyComment.getUser().getId())  // User의 ID
                .nickname(storyComment.getUser().getNickname()) // User의 닉네임
                .seq(storyComment.getStory().getSeq())  // Story의 seq
                .commentcontent(storyComment.getCommentcontent())
                .build();
    }

    @Override
    public void removeComment(Long commentId) {
        storyCommentRepository.deleteById(commentId);  // 댓글 ID로 댓글 삭제
    }

    @Override
    public void incrementCommentCount(long seq) {
        MeongStory story = meongStoryRepository.findById(seq)
                .orElseThrow(() -> new RuntimeException("Story not found with seq: " + seq));
        story.setCommentcount(story.getCommentcount() + 1); // viewcount 증가
        meongStoryRepository.save(story); // 업데이트
    }

    @Override
    public void decrementCommentCount(long seq) {
        MeongStory story = meongStoryRepository.findById(seq)
                .orElseThrow(() -> new RuntimeException("Story not found with seq: " + seq));
        story.setCommentcount(story.getCommentcount() - 1); // viewcount 증가
        meongStoryRepository.save(story); // 업데이트
    }

    @Transactional
    public int likeComment(Long commentId, String uid) {
        StoryComment comment = storyCommentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));

        User user = userRepository.findByUid(uid)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));

        // 유저가 이미 해당 댓글에 좋아요를 눌렀는지 확인
        if (comment.getLikedUserIds().contains(user.getId())) {
            throw new IllegalStateException("이미 좋아요를 누른 댓글입니다.");
        }

        // 좋아요 추가
        comment.addLike(user.getId());
        storyCommentRepository.save(comment);  // 좋아요 정보 저장

        return comment.getLikeCount();  // 업데이트된 좋아요 수 반환
    }

    @Transactional
    public void addReply(Long commentId, Long userId, String replyContent) {
        StoryComment comment =  storyCommentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));

        // 대댓글 추가
        StoryReply storyreply = new StoryReply(userId,replyContent);
        comment.addReply(storyreply);

        storyCommentRepository.save(comment);
    }

    public List<StoryReply> getRepliesByCommentId(Long commentId) {
        StoryComment comment =  storyCommentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));
        return comment.getReplies();
    }

    public void deleteReply(Long commentId, String replyId) {
        StoryComment comment =storyCommentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));

        // 해당 대댓글을 ID로 찾고 리스트에서 제거
        List<StoryReply> replies = comment.getReplies();
        replies.removeIf(reply -> reply.getId().equals(replyId));

        // 변경된 댓글을 다시 저장
        comment.setReplies(replies);
        storyCommentRepository.save(comment);
    }


}