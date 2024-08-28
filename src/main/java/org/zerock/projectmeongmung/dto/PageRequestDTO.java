package org.zerock.projectmeongmung.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Builder
@AllArgsConstructor
@Data
public class PageRequestDTO {

    private int page; // 현재 페이지 정보를 관리하는 변수
    private int size; // 한블록에 게시글 몇 개씩 출력하겠냐는 것
    private String type;
    private String keyword;

    public PageRequestDTO() {
        this.page = 1;
        this.size = 10;
    }

    public Pageable getPageable(Sort sort) {
        return PageRequest.of(page - 1, size, sort);
    }

    // 정렬이 없는 기본 getPageable 메서드
    public Pageable getPageable() {
        return PageRequest.of(page - 1, size);
    }
}
