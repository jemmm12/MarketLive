package com.ssafy.rtc.video;

import com.ssafy.rtc.util.GlobalConstants;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@EnableWebSocket
public class RoomConnection implements WebSocketConfigurer {

    private RtcHandler rtcHandler;
    private String broadCasterUserId;

    public RoomConnection(long broadCasterUserId) {
        this.rtcHandler = new RtcHandler();
        this.broadCasterUserId = String.valueOf(broadCasterUserId);
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(rtcHandler, GlobalConstants.SOCKET_URL + broadCasterUserId);
    }
}
