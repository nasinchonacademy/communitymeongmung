package org.zerock.projectmeongmung.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.zerock.projectmeongmung.entity.MeongStory;

import java.util.List;

@Repository
public interface MeongStoryRepository extends JpaRepository<MeongStory, Long>, QuerydslPredicateExecutor<MeongStory> {

    List<MeongStory> findByUserId(Long userId);
    Page<MeongStory> findByCategory(String category, Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE MeongStory m SET m.likecount = m.likecount + 1 WHERE m.seq = :seq")
    void incrementLikeCount(@Param("seq") Long seq);

    @Query("SELECT m FROM MeongStory m WHERE m.title LIKE %:keyword% OR m.content LIKE %:keyword%")
    Page<MeongStory> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    // 키워드와 카테고리로 검색하는 메서드 추가
    @Query("SELECT m FROM MeongStory m WHERE (m.title LIKE %:keyword% OR m.content LIKE %:keyword%) AND m.category = :category")
    Page<MeongStory> searchByKeywordAndCategory(@Param("keyword") String keyword, @Param("category") String category, Pageable pageable);

    @Query("SELECT m FROM MeongStory m WHERE m.category = 'Category1' AND (m.title LIKE CONCAT('%', :keyword, '%') OR m.content LIKE CONCAT('%', :keyword, '%'))")
    Page<MeongStory> searchPetFriendlyLocationsByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT m FROM MeongStory m WHERE m.category = 'Category2' AND (m.title LIKE CONCAT('%', :keyword, '%') OR m.content LIKE CONCAT('%', :keyword, '%'))")
    Page<MeongStory> searchDailyItemsByKeyword(@Param("keyword") String keyword, Pageable pageable);



/*    @Query("SELECT m FROM MeongStory m WHERE (m.title LIKE %:keyword% OR m.content LIKE %:keyword%  LIKE %:keyword%) AND m.category = :category")*/

    /**/
}
