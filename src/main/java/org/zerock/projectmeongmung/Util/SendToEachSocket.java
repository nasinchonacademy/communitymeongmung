package org.zerock.projectmeongmung.Util;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.Set;

public class SendToEachSocket {

    public static void sendToEachSocket(Set<WebSocketSession> sessions, TextMessage message) {
        sessions.stream()
                .filter(WebSocketSession::isOpen) // 열려 있는 세션만 필터링
                .forEach(session -> {
                    try {
                        session.sendMessage(message); // 메시지를 세션으로 전송
                    } catch (IOException e) {
                        e.printStackTrace(); // 전송 실패 시 예외 처리
                    }
                });
    }
}
