package org.zerock.projectmeongmung.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.projectmeongmung.dto.AddUserRequest;
import org.zerock.projectmeongmung.dto.SOSBoardCommentDto;
import org.zerock.projectmeongmung.dto.StoryCommentDto;
import org.zerock.projectmeongmung.entity.*;
import org.zerock.projectmeongmung.repository.SOSBoardCommentRepository;
import org.zerock.projectmeongmung.repository.SOSboardRepository;
import org.zerock.projectmeongmung.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SOSBoardCommentService {

    private final UserRepository userRepository;
    private final SOSBoardCommentRepository SOSBoardCommentRepository;
    private final SOSboardRepository sosboardrepository;

    public SOSBoardCommentDto Commentread(Long seq){
        //특정 댓글을 가져와서 DTO로 변환
        return SOSBoardCommentRepository.findById(seq)
                .map(this::entityToDto)
                .orElse(null);

    }

    public List<SOSBoardCommentDto> getCommentsByBoardId(Long seq){
        // 특정 게시글에 속한 모든 댓글을 가져와서 DTO로 변환
        List<SOSboardcomment> comment = SOSBoardCommentRepository.findBySosboard_Sosboardseq(seq);
        return comment.stream()
                .map(this ::entityToDto)
                .collect(Collectors.toList());
    }

    public void CommentRegister(SOSBoardCommentDto commentDto) {
        // 댓글 등록 로직
        SOSboardcomment comment = SOSboardcomment.builder()
                .soscommentcontent(commentDto.getCommentcontent())
                .user(userRepository.findById(commentDto.getId())
                        .orElseThrow(() -> new RuntimeException("User not found")))
                .sosboard( sosboardrepository.findById(commentDto.getSeq())
                        .orElseThrow(() -> new RuntimeException("Story not found")))
                .build();

        SOSBoardCommentRepository.save(comment);
    }

    // 엔티티를 DTO로 변환하는 메서드
    private SOSBoardCommentDto entityToDto(SOSboardcomment sosboardcomment) {
        return SOSBoardCommentDto.builder()
                .commentid(sosboardcomment.getCommentid())
                .id(sosboardcomment.getUser().getId())  // User의 ID
                .nickname(sosboardcomment.getUser().getNickname()) // User의 닉네임
                .seq(sosboardcomment.getSosboard().getSosboardseq())  // Story의 seq
                .commentcontent(sosboardcomment.getSoscommentcontent())
                .build();
    }

    @Transactional
    public void removeComment(Long commentId) {
        SOSboardcomment comment =  SOSBoardCommentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));

        // 게시물에서 댓글 제거 및 댓글 수 업데이트
        SOSboard sosboard = comment.getSosboard();
        sosboard.getComments().remove(comment);
        sosboard.setCommentcount(sosboard.getComments().size());

        SOSBoardCommentRepository.delete(comment);
    }

    @Transactional
    public int likeComment(Long commentId, String uid) {
        SOSboardcomment comment = SOSBoardCommentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));

        User user = userRepository.findByUid(uid)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));

        // 유저가 이미 해당 댓글에 좋아요를 눌렀는지 확인
        if (comment.getLikedUserIds().contains(user.getId())) {
            throw new IllegalStateException("이미 좋아요를 누른 댓글입니다.");
        }

        // 좋아요 추가
        comment.addLike(user.getId());
        SOSBoardCommentRepository.save(comment);  // 좋아요 정보 저장

        return comment.getLikeCount();  // 업데이트된 좋아요 수 반환
    }

    @Transactional
    public void addReply(Long commentId, Long userId, String replyContent) {
        SOSboardcomment comment =  SOSBoardCommentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));

        // 대댓글 추가
        Reply reply = new Reply(userId, replyContent);
        comment.addReply(reply);

        SOSBoardCommentRepository.save(comment);
    }

    public List<Reply> getRepliesByCommentId(Long commentId) {
        SOSboardcomment comment =  SOSBoardCommentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));
        return comment.getReplies();
    }

    public void deleteReply(Long commentId, String replyId) {
        SOSboardcomment comment = SOSBoardCommentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));

        // 해당 대댓글을 ID로 찾고 리스트에서 제거
        List<Reply> replies = comment.getReplies();
        replies.removeIf(reply -> reply.getId().equals(replyId));

        // 변경된 댓글을 다시 저장
        comment.setReplies(replies);
        SOSBoardCommentRepository.save(comment);
    }

}