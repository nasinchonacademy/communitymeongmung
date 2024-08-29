package org.zerock.projectmeongmung.dto;

import lombok.Builder;
import lombok.Data;
import org.zerock.projectmeongmung.entity.MeongStory;

import java.time.LocalDateTime;

@Builder
@Data
public class MeongStoryDTO {
    private Long seq;
    private Long idstory;
    private String title;
    private int likecount;
    private String content;
    private LocalDateTime regdate;
    private LocalDateTime modified;
    private String picture;
    private int commentcount;
    private int viewcount;
    private LocalDateTime deleted;
    private String category;
    private String uid;
    private String nickname;

    public MeongStoryDTO(Long seq, Long idstory, String title, int likecount, String content,
                         LocalDateTime regdate, LocalDateTime modified, String picture,
                         int commentcount, int viewcount, LocalDateTime deleted,
                         String category, String uid, String nickname) {
        this.seq = seq;
        this.idstory = idstory;
        this.title = title;
        this.likecount = likecount;
        this.content = content;
        this.regdate = regdate;
        this.modified = modified;
        this.picture = picture;
        this.commentcount = commentcount;
        this.viewcount = viewcount;
        this.deleted = deleted;
        this.category = category;
        this.uid = uid;
        this.nickname = nickname;
    }

    public static MeongStoryDTO fromEntity(MeongStory story) {
        return MeongStoryDTO.builder()
                .seq(story.getSeq())
                .title(story.getTitle())
                .likecount(story.getLikecount())
                .content(story.getContent())
                .regdate(story.getRegdate())
                .modified(story.getModified())
                .picture(story.getPicture())
                .commentcount(story.getCommentcount())
                .viewcount(story.getViewcount())
                .deleted(story.getDeleted())
                .category(story.getCategory())
                .uid(story.getUser().getUid())
                .nickname(story.getUser().getNickname())
                .build();
    }
}
