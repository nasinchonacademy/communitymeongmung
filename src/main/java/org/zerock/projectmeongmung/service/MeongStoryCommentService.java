package org.zerock.projectmeongmung.service;

import org.zerock.projectmeongmung.dto.StoryCommentDto;
import org.zerock.projectmeongmung.entity.Reply;
import org.zerock.projectmeongmung.entity.StoryComment;
import org.zerock.projectmeongmung.entity.StoryReply;

import java.util.List;

public interface MeongStoryCommentService {

    StoryCommentDto Commentread(Long seq);  // 반환 타입: StoryCommentDto

    List<StoryCommentDto> getCommentsByStorySeq (Long seq);

    void CommentRegister(StoryCommentDto commentDto);  // 반환 타입: void

    void removeComment(Long commentId);

    void incrementCommentCount(long seq);

    void decrementCommentCount(long seq);

    int likeComment(Long commentId, String username);

    void addReply(Long commentId, Long userId, String replyContent);

    List<StoryReply> getRepliesByCommentId(Long commentId);

    void deleteReply(Long commentId, String replyId);
}