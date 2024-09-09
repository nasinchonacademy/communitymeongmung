package org.zerock.projectmeongmung.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@RequiredArgsConstructor
//Lombok의 어노테이션으로, 클래스의 final 필드에 대한 생성자를 자동으로 생성합니다. 여기서는 WebSocketHandler를 주입받기 위해 사용
@Configuration
// Spring의 설정 클래스를 나타냅니다. 이 클래스가 Spring의 애플리케이션 컨텍스트에 의해 구성될 수 있도록 함
@EnableWebSocket
// Websocket 서버로서 동작하겠다는 어노테이션
public class WebsocketConfig implements WebSocketConfigurer {
    private final WebSocketHandler webSocketHandler;
    //WebSocketHandler 타입의 필드로, 웹소켓 메시지를 처리하는 핸들러입니다. 생성자를 통해 주입됩니다.

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry){
        registry.addHandler(webSocketHandler,"/ws/chat").setAllowedOrigins("*");
        //WebSocketConfigurer 인터페이스에서 정의된 메서드를 오버라이드하여 웹소켓 핸들러를 등록합니다.
        //webSocketHandler: 웹소켓 요청을 처리할 핸들러, "/ws/chat": 웹소켓 엔드포인트 URL. 클라이언트는 이 URL로 웹소켓 연결을 시도합니다.
        //.setAllowedOrigins("*"): CORS(Cross-Origin Resource Sharing) 설정을 통해 모든 출처에서의 요청을 허용합니다.
    }
}

//위에서 만든 handler를 이용하여 Websocket을 활성화하기 위한 Config 파일을 작성합니다.
//@EnableWebSocket을 선언하여 Websocket을 활성화합니다. Websocket에 접속하기 위한 endpoint는 /ws/chat으로
//설정하고 도메인이 다른 서버에서도 접속 가능하도록 CORS : setAllowedOrigins(“*”)를 설정을 추가해 줍니다.
//이제 클라이언트가 ws://localhost:8080/ws/chat으로 커넥션을 연결하고 메시지 통신을 할 수 있는 기본적인 준비가
//끝났습니다. 나머지는 채팅메세지를 위한 DTO,  채팅 방을 위한 ChatRoom 및 Chatroom 관련 service
//그리고 화면을 위한 Controller, html 등만 작성하면 됩니다.