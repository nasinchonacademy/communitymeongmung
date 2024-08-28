package org.zerock.projectmeongmung;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ProjectMeongmungApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjectMeongmungApplication.class, args);
    }
}