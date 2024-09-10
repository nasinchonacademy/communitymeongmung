package org.zerock.projectmeongmung.dto;

public class NoticeDTO {
    private String message;

    public NoticeDTO(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}