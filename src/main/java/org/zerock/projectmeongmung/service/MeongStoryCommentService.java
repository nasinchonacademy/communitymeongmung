package org.zerock.projectmeongmung.service;

import org.zerock.projectmeongmung.dto.StoryCommentDto;
import org.zerock.projectmeongmung.entity.StoryComment;

import java.util.List;

public interface MeongStoryCommentService {
    StoryCommentDto Commentread(Long seq);  // 반환 타입: StoryCommentDto
    List<StoryCommentDto> getCommentsByStorySeq (Long seq);
    void CommentRegister(StoryCommentDto commentDto);  // 반환 타입: void
    void removeComment(Long commentId);
}