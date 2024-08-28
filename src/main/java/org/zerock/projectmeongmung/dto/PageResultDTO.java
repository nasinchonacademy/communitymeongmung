package org.zerock.projectmeongmung.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class PageResultDTO<DTO, EN> {

    // DTO 리스트
    private List<DTO> dtoList;

    // 총 페이지 번호
    private int totalPage;

    // 현재 페이지 번호
    private int page;

    // 목록 사이즈
    private int size;

    // 시작 페이지 번호, 끝 페이지 번호
    private int start, end;

    // 이전, 다음 여부
    private boolean prev, next;

    // 블록의 페이지 번호 목록
    private List<Integer> pageList;

    public PageResultDTO(Page<EN> result, Function<EN, DTO> fn) {
        // Entity -> DTO로 변환
        dtoList = result.stream().map(fn).collect(Collectors.toList());

        // 총 페이지 수
        totalPage = result.getTotalPages();

        // 페이지 목록 생성
        makePageList(result.getPageable());
    }

    private void makePageList(Pageable pageable) {
        this.page = pageable.getPageNumber() + 1; // 0부터 시작하므로 1을 더함
        this.size = pageable.getPageSize();

        // 임시 끝 페이지 번호 계산
        int tempEnd = (int) (Math.ceil(page / 10.0)) * 10;

        this.start = tempEnd - 9;

        // 실제 끝 페이지 번호를 결정
        this.end = Math.min(totalPage, tempEnd);

        // 이전 페이지 여부
        prev = start > 1;

        // 다음 페이지 여부
        next = totalPage > tempEnd;

        // 페이지 번호 목록 생성
        pageList = IntStream.rangeClosed(start, end).boxed().collect(Collectors.toList());
    }
}
