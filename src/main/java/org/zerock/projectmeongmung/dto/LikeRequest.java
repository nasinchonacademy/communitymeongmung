package org.zerock.projectmeongmung.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LikeRequest {
    private Long seq; // 좋아요를 누를 게시물의 ID
}
