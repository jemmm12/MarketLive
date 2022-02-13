package com.ssafy.rtc.video;

import com.ssafy.rtc.util.GlobalConstants;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@EnableWebSocket
public class RoomConnection implements WebSocketConfigurer {

    private String broadCasterUserId;
    public RtcHandler rtcHandler;

    public RoomConnection(long broadCasterUserId) {
        this.broadCasterUserId = Long.toString(broadCasterUserId);
        this.rtcHandler = new RtcHandler();
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(rtcHandler, GlobalConstants.SOCKET_URL + broadCasterUserId).setAllowedOrigins("*");
    }
}
