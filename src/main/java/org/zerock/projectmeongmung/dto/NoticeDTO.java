package org.zerock.projectmeongmung.dto;

public class NoticeDTO {
    private Long id;  // 알림의 고유 ID 추가
    private String message;

    public NoticeDTO(Long id, String message) {
        this.id = id;
        this.message = message;
    }

    public Long getId() {
        return id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
