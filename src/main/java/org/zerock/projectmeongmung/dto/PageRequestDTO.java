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

    private int page;        // 현재 페이지 정보를 관리하는 변수
    private int size;        // 한 블록에 게시글 몇 개씩 출력할지 결정하는 변수
    private String type;     // 검색 타입
    private String keyword;  // 검색 키워드
    private String category; // 카테고리 필드
    private boolean isSubmitted; // 폼 제출 여부를 저장하는 필드

    // 기본 생성자: 기본값으로 페이지 1, 크기 10을 설정
    public PageRequestDTO() {
        this.page = 1;
        this.size = 10;
    }

    // 정렬 기준을 포함한 Pageable 객체 반환
    public Pageable getPageable(Sort sort) {
        return PageRequest.of(page - 1, size, sort);
    }

    // 정렬이 없는 기본 Pageable 객체 반환
    public Pageable getPageable() {
        return PageRequest.of(page - 1, size);
    }

    // isSubmitted 필드에 대한 Getter와 Setter
    public boolean isSubmitted() {
        return isSubmitted;
    }

    public void setSubmitted(boolean isSubmitted) {
        this.isSubmitted = isSubmitted;
    }
}
