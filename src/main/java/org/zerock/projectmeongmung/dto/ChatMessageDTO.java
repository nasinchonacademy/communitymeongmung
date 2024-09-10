package org.zerock.projectmeongmung.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatMessageDTO {

    public enum MessageType{
        ENTER, TALK, QUIT
        // 메세지 타입 : 입장, 채팅, 나감
    }
    private MessageType type; // 메세지 타입을 나타내는 것
    private String roomId; // 방 번호를 나타내는 것
    private String nickname; // 메세지 보낸 사람을 나타내는 것 // Sender 원래
    private String message; // 메세지
}
