package org.zerock.projectmeongmung.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@Embeddable
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Reply {

    private Long userId;  // 작성자의 사용자 ID
    private String replyContent;  // 대댓글 내용
    private Date replyRegtime;  // 대댓글 작성 시간

    // 대댓글 작성 시 시간을 자동으로 넣어주기 위해 설정
    public Reply(Long userId, String replyContent) {
        this.userId = userId;
        this.replyContent = replyContent;
        this.replyRegtime = new Date();
    }
}
