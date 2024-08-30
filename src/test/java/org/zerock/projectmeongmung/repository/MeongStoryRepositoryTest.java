package org.zerock.projectmeongmung.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.zerock.projectmeongmung.entity.MeongStory;
import org.zerock.projectmeongmung.entity.QMeongStory;
import org.zerock.projectmeongmung.entity.User;

import java.util.Optional;
import java.util.stream.IntStream;

@SpringBootTest
public class MeongStoryRepositoryTest {

    @Autowired
    private MeongStoryRepository meongStoryRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void insertDummies() {
        // User 객체를 UID를 통해 DB에서 조회
        Optional<User> userOptional = userRepository.findByUid("test1");

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // MeongStory 엔터티를 생성하고 해당 사용자와 연관시켜 저장
            IntStream.rangeClosed(1, 300).forEach(i -> {
                MeongStory meongStory = MeongStory.builder()
                        .title("Title..." + i)                   // 제목 설정
                        .content("Content" + i)                  // 내용 설정
                        .likecount(i)                            // 좋아요 수 설정
                        .commentcount(i)                         // 댓글 수 설정
                        .category("Category" + 1)                // 카테고리 설정
                        .viewcount(i)                            // 조회수 설정
                        .picture("/image/tool/storydefault.PNG") // 이미지 경로 설정
                        .user(user)                              // User 객체와 연관 설정
                        .build();

                // MeongStory 엔터티를 DB에 저장
                meongStoryRepository.save(meongStory);
            });
        } else {
            System.out.println("User not found");
        }
    }


    @Test
    public void testQuery1() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("seq").descending());

        QMeongStory qMeongStory = QMeongStory.meongStory;

        String keyword = "1";

        BooleanBuilder builder = new BooleanBuilder();

        BooleanExpression expression = qMeongStory.title.contains(keyword);

        builder.and(expression);

        Page<MeongStory> result = meongStoryRepository.findAll(builder, pageable);

        result.stream().forEach(meongStory -> System.out.println("페이지 : " + meongStory));
    }
}
