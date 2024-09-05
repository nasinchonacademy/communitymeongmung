package org.zerock.projectmeongmung.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zerock.projectmeongmung.entity.StoryComment;

import java.util.List;
import java.util.Optional;

@Repository
public interface StoryCommentRepository extends JpaRepository<StoryComment, Long> {

    // 특정 게시글(seq)에 속하는 모든 댓글을 찾는 메서드
    List<StoryComment> findByStorySeq(Long seq);

    Optional<StoryComment> findByStory_SeqAndUser_Id(Long seq, Long userId);

}