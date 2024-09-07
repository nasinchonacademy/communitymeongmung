package org.zerock.projectmeongmung.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class SOSBoardCommentDto {

    private Long commentid;
    private Long id;  // 사용자 ID
    private String nickname;  // 사용자 닉네임 추가
    private Long seq;  // 게시글 ID
    private String commentcontent;
    private LocalDateTime regdate;
    private LocalDateTime modified;

    public SOSBoardCommentDto(Long commentid, Long id, String nickname, Long seq, String commentcontent,
                           LocalDateTime regdate, LocalDateTime modified) {
        this.commentid = commentid;
        this.id = id;
        this.nickname = nickname;  // 닉네임 필드 추가
        this.seq = seq;
        this.commentcontent = commentcontent;
        this.regdate = regdate;
        this.modified = modified;
    }

    public SOSBoardCommentDto() {

    }


}
