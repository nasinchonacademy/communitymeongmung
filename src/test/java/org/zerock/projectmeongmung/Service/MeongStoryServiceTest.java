package org.zerock.projectmeongmung.Service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.projectmeongmung.dto.MeongStoryDTO;
import org.zerock.projectmeongmung.entity.User;
import org.zerock.projectmeongmung.repository.UserRepository;
import org.zerock.projectmeongmung.service.MeongStoryService;

@SpringBootTest
public class MeongStoryServiceTest {

    @Autowired
    private MeongStoryService service;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setup() {
        // 테스트용 사용자 추가
        if (!userRepository.findByUid("testUserUid").isPresent()) {
            User user = User.builder()
                    .uid("testUserUid")
                    .nickname("testNickname")
                    .email("test@example.com")
                    .password("password")
                    .name("Test User")
                    .build();

            userRepository.save(user);
        }
    }

    @Test
    public void testRegister() {
        MeongStoryDTO meongStoryDTO = MeongStoryDTO.builder()
                .title("Sample Title")
                .content("Sample Content...")
                .uid("testUserUid")  // setup() 메서드에서 추가한 유효한 uid 사용
                .category("Sample Category")  // 카테고리 설정
                .build();

        System.out.println(service.register(meongStoryDTO));
    }

}
