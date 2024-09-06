package org.zerock.projectmeongmung.dto;

import lombok.*;
import org.zerock.projectmeongmung.entity.SOSboard;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SOSboardDTO {

    private Long sosboardseq; // 보드의 고유 ID

    private Long userId; // 사용자 ID (User 엔티티의 ID를 담기 위해)

    private String title; // 보드의 제목

    private String content; // 보드의 내용

    private Date regdate; // 등록일

    private Date moddate; // 수정일

    private String picture; // 이미지

    private int commentcount=0; // 댓글 수

    private int viewcount=0; // 조회 수

    private Date deldate; // 삭제일

    private String nickname;

    private String uid;

    private int likecount=0;



    public SOSboardDTO(SOSboard sosboard) {
        this.nickname = sosboard.getUser().getNickname();
        this.uid = sosboard.getUser().getUid();
    }
}