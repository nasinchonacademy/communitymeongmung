    package org.zerock.projectmeongmung;

    import org.junit.jupiter.api.Test;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.boot.test.context.SpringBootTest;
    import org.zerock.projectmeongmung.entity.MeongStory;
    import org.zerock.projectmeongmung.entity.User;
    import org.zerock.projectmeongmung.repository.MeongStoryRepository;
    import org.zerock.projectmeongmung.repository.UserRepository;


    import java.util.Optional;
    import java.util.stream.IntStream;

    @SpringBootTest
    class ProjectMeongmungApplicationTests {

        @Autowired
        private UserRepository userRepository;

        @Autowired
        private MeongStoryRepository meongStoryRepository;

        @Test
        public void insertDummies() {
            // User 객체를 UID를 통해 DB에서 조회
            Optional<User> userOptional = userRepository.findByUid("test3");

            if (userOptional.isPresent()) {
                User user = userOptional.get();

                // MeongStory 엔터티를 생성하고 해당 사용자와 연관시켜 저장
                IntStream.rangeClosed(50, 100).forEach(i -> {
                    MeongStory meongStory = MeongStory.builder()
                            .title("Test" + i)                   // 제목 설정
                            .content("Content" + i)                  // 내용 설정
                            .likecount(0)                            // 좋아요 수 설정
                            .commentcount(0)                         // 댓글 수 설정
                            .category("Category" + 2)                // 카테고리 설정
                            .viewcount(0)                            // 조회수 설정
                            .picture(null)        // 이미지 경로 설정
                            .user(user)                              // User 객체와 연관 설정
                            .build();

                    // MeongStory 엔터티를 DB에 저장
                    meongStoryRepository.save(meongStory);
                });
            } else {
                System.out.println("User not found");
            }
        }

    }