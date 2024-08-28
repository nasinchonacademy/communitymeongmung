package org.zerock.projectmeongmung.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import org.zerock.projectmeongmung.entity.MeongStory;

import java.util.List;

@Repository
public interface MeongStoryRepository extends JpaRepository<MeongStory, Long>, QuerydslPredicateExecutor<MeongStory> {
    List<MeongStory> findByUserId(Long userId);
    Page<MeongStory> findByCategory(String category, Pageable pageable);

}
