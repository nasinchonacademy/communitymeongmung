package org.zerock.projectmeongmung.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.zerock.projectmeongmung.entity.StoryLike;
import org.zerock.projectmeongmung.entity.User;

import java.util.List;

@Repository
public interface StoryLikeRepository extends JpaRepository<StoryLike, Long> {
    List<StoryLike> findByUser(User user);
}
