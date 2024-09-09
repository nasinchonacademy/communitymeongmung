package org.zerock.projectmeongmung.Handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.zerock.projectmeongmung.dto.ChatMessageDTO;
import org.zerock.projectmeongmung.dto.ChatRoomDTO;
import org.zerock.projectmeongmung.service.ChatService;

import java.io.IOException;
import java.util.Set;

import static org.zerock.projectmeongmung.Util.SendToEachSocket.sendToEachSocket;

@Slf4j
@RequiredArgsConstructor
@Component
public class WebsocketHandler extends TextWebSocketHandler {
    private final ObjectMapper objectMapper;
    private final ChatService chatService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
    // WebSocketSession 인터페이스는 웹소켓에서 웹소켓 통신중에 서버와 클라이언트 간의 세션을 관리하기 위한 인터페이스이다
    // 이를 통해서, 우리는 양방향 통신을 할 수 있다.
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception{
        // protect 는 접근 제어자로서, 이 메서드가 같은 패키지, 또는 상속받은 클래스에서 접근할 수 있음
        // handleTextMessage 이 메서드는 텍스트 메시지를 처리한다는 의미
        // WebSocketSession 객체는, 서버간의 웹소켓 세션을 나타낸다, 클라이언트와의 메세지 송수신을 제어할 수 있다.
        // TextMessage message는 웹소켓을 통해 전달된 텍스트 메세지를 나타내는 객체이다.
        // message.getPayload()을 사용하여, 메세지의 본문을 가져올 수 있다.


        String payload = message.getPayload();
        // 웹소켓 메세지에서 실제 전송된 데이터 내용을 추출하는 코드
        ChatMessageDTO chatMessage = objectMapper.readValue(payload, ChatMessageDTO.class);
        //Jackson 라이브러리를 사용하여, json 형태의 문자열 payload를 ChatMessageDTO 객체로 변환하는 코드
        // 그렇기 때문에 클라이언트가 WebSocket으로 보낸 Json 메세지를 Java 객체로 매핑하여 사용할 수 있게 된다.
        ChatRoomDTO room = chatService.findRoomById(chatMessage.getRoomId());
        // room은 findRoomById() 메서드 호출을 통해 반환된 ChatRoomDTO 객체를 담는다.
        // chatService는 내부적으로 저장된 여러 채팅방 (Map, List) 중에서 roomId에 해당하는 방을 찾아 반환할 수 있다.
        // (chatMessage.getRoomId())는 클라이언트로부터 전달된 채팅 메세지를 나타내는 객체다.
        // 그 객체 안에서 getRoomId 는 이 메세지가 속한 "채팅방의 ID"를 가져온다 (반환한다)
        // 이를 통해 서버는 메세지를 어떤 채팅방에 속하는지 파악하고,해당 채팅방에 대한 처리를 진행하게 해준다.

        Set<WebSocketSession> sessions = room.getSessions();
        // session 는 WebSocketSession 객체의 집합 Set이다.
        // WebSocketSession은 스프링 웹소켓에서 서버와 클라이언트 간의 웹소켓 연결을 나타내는 세션 객체이다.
        // set 자료구조는 중복된 세션을 허용하지 않기 때문에, 한번 연결된 클라이언트는 Set에 중복으로 들어가지 않는다.
        // room 은 ChatRoomDTO 로 특정 채팅방을 나타냄 (아까 아이디를 가져왔으니)
        // getSessions 는 해당 채팅방에 연결된 모든 클라이언트의 웹소켓 세션을 가져오는 (반환하는) 메서드이다.

        // ================================================================================
        // 이를 통해 채팅방에 "접속중인" 클라이언트 목록을 확인할 수 있다. // 나중에 개발할거 확인
        // ================================================================================

        if (chatMessage.getType().equals(ChatMessageDTO.MessageType.ENTER)) {
            // ChatMessageDTO.MessageType.ENTER 는 메세지 타입중 ENTER을 나타낸다.
            // 이는 보통 사용자가 채팅방에 입장할 때 발생하는 메세지 타입이다.
            sessions.add(session);
            chatMessage.setMessage(chatMessage.getNickname() + "님이 입장했습니다.");
            sendToEachSocket(sessions,new TextMessage(objectMapper.writeValueAsString(chatMessage)) );
            // 입장 처리 메세지
        }else if (chatMessage.getType().equals(ChatMessageDTO.MessageType.QUIT)) {
            sessions.remove(session);
            chatMessage.setMessage(chatMessage.getNickname() + "님이 퇴장했습니다..");
            sendToEachSocket(sessions,new TextMessage(objectMapper.writeValueAsString(chatMessage)) );
            // 이는 퇴장이 되었을 때 퇴정 처리 메세지이다.
        }else {
            sendToEachSocket(sessions,message );
            //입장,퇴장 아닐 때는 클라이언트로부터 온 메세지 그대로 전달.
        }
    }
    private void sendToEachSocket(Set<WebSocketSession> sessions, TextMessage message){
        //sendToEachSocket 메서드는 Set<WebSocketSession> 에 포함된 모든 세션에 대해 주어진 TextMessage를 비 동기적으로 전송하는 기능
        sessions.parallelStream().forEach(roomSession -> {
        //sessions 집합을 parallelSteam() 으로 변환하여 병렬 스트림을 생성한다.
        //병렬 스트림은 여러 스레드를 사용하기에 작업을 동시에 수행한다. 대량의 데이터를 빠르게 처리할 수 있음.
        //WebSocketSession 객체에 대해 주어진 작업을 수행한다. 여기서는 roomSession이 각 세션을 나타낸다.
        //모든 세션에 대해 메세지를 전송하는 작업을 수행한다.
            try {
                roomSession.sendMessage(message);
        //설명: try 블록 내의 코드는 예외가 발생할 가능성이 있는 코드입니다. 여기서는 roomSession.sendMessage(message) 메서드 호출
            } catch (IOException e){
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        //javascript에서  session.close해서 연결 끊음. 그리고 이 메소드 실행.
        //session은 연결 끊긴 session을 매개변수로 이거갖고 뭐 하세요.... 하고 제공해주는 것 뿐
    }
}

//일반적으로 채팅동작하는 원리는 다음과 같다.
//handletextMessage 는  사용자(javascript에서 websocket 객체)가   send("메세지")를 실행하면 호출된다.서버는 한 사용자가 보낸 메세지(TextMessage message)를  다른사용자'들' 에게 보낸다.
//여기서는 입장과 퇴장, 그 외를 나눠서 서로다른메세지를 세팅했지만, sendToEachSocket() 메소드를 통해 같은방에 있는 모든 사용자들에게 메세지를 보낸다.