package org.zerock.projectmeongmung.dto;

import lombok.Builder;
import lombok.Data;
import org.zerock.projectmeongmung.entity.StoryComment;

import java.time.LocalDateTime;

@Data
@Builder
public class StoryCommentDto {

    private Long commentid;
    private String id2;
    private Long seq;
    private String commentcontent;
    private LocalDateTime commentregitime;
    private LocalDateTime commentupdate;

    public StoryCommentDto(Long commentid, String id2, Long seq, String commentcontent, LocalDateTime commentregitime, LocalDateTime commentupdate) {
        this.commentid = commentid;
        this.id2 = id2;
        this.seq = seq;
        this.commentcontent = commentcontent;
        this.commentregitime = commentregitime;
        this.commentupdate = commentupdate;
    }

    public static StoryCommentDto fromEntity(StoryComment story) {
        return StoryCommentDto.builder()  // 여기에서 MeongStoryDTO.builder() 대신 StoryCommentDto.builder()를 사용합니다.
                .commentid(story.getCommentid())
                .id2(story.getUser().getUid())  // 적절한 사용자 ID 필드로 변경
                .seq(story.getStory().getSeq())  // MeongStory 객체에서 seq를 가져옵니다.
                .commentcontent(story.getCommentcontent())
                .commentregitime(story.getCommentregitime())
                .commentupdate(story.getCommentupdate())
                .build();
    }
}
