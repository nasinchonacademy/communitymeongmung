package org.zerock.projectmeongmung.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.projectmeongmung.dto.StoryCommentDto;
import org.zerock.projectmeongmung.entity.MeongStory;
import org.zerock.projectmeongmung.repository.MeongStoryRepository;
import org.zerock.projectmeongmung.service.MeongStoryCommentService;

import java.util.Map;

@Controller
@RequestMapping("/comment")
@Log4j2
@RequiredArgsConstructor
public class CommentController {

    private final MeongStoryCommentService service;
    private final MeongStoryRepository meongStoryRepository;

/*댓글 메소드 있던 곳*/

   /* // 댓글을 등록하는 메서드
    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addComment(@RequestParam("seq") Long seq,
                                                          @RequestParam("commentcontent") String commentContent,
                                                          @RequestParam("userId") Long userId, // 사용자 ID
                                                          @RequestParam("nickname") String nickname) {
        log.info("Adding comment to story seq: " + seq);

        // 댓글을 달려는 게시글을 찾습니다.
        MeongStory story = meongStoryRepository.findById(seq).orElse(null);

        if (story == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Story not found"));
        }

        // 댓글 등록
        StoryCommentDto commentDto = StoryCommentDto.builder()
                .seq(seq)
                .commentcontent(commentContent)
                .id(userId)  // 사용자 ID 설정
                .nickname(nickname)  // 사용자 닉네임 설정
                .build();

        service.CommentRegister(commentDto);

        // 댓글 수를 증가시키고 저장
        story.setCommentcount(story.getCommentcount() + 1);
        meongStoryRepository.save(story);

        // 새로운 댓글 수를 반환
        return ResponseEntity.ok(Map.of("commentcount", story.getCommentcount()));
    }*/
}
