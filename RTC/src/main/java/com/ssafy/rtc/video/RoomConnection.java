package com.ssafy.rtc.video;

import com.ssafy.rtc.util.GlobalConstants;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@EnableWebSocket
public class RoomConnection implements WebSocketConfigurer {

    private String broadCasterUserId;

    public RoomConnection(long broadCasterUserId) {
        this.broadCasterUserId = String.valueOf(broadCasterUserId);
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new RtcHandler(), GlobalConstants.SOCKET_URL + broadCasterUserId).setAllowedOrigins("*");
    }
}
