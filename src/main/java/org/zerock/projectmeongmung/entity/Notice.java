package org.zerock.projectmeongmung.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    private String message;

    @Setter
    @Getter
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 기본 생성자
    public Notice() {}

    // 파라미터가 있는 생성자
    public Notice(String message, User user) {
        this.message = message;
        this.user = user;
    }

    // Getter 메서드
    public String getMessage() {
        return message;
    }

}
