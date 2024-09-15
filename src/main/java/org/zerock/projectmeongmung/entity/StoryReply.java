package org.zerock.projectmeongmung.entity;


import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Embeddable
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StoryReply {

    private Long userId;  // 작성자의 사용자 ID
    private String replyContent;  // 대댓글 내용
    private Date replyRegtime;  // 대댓글 작성 시간
    private String id;  // 고유 식별자 (UUID로 설정)

    // 대댓글 작성 시 시간을 자동으로 넣어주기 위해 설정
    public StoryReply(Long userId, String replyContent) {
        this.userId = userId;
        this.replyContent = replyContent;
        this.replyRegtime = new Date();
        this.id = UUID.randomUUID().toString();  // UUID로 고유 ID 설정
    }

}
