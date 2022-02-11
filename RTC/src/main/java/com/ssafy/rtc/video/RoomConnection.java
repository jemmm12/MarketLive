package com.ssafy.rtc.video;

import com.ssafy.rtc.util.GlobalConstants;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

public class RoomConnection implements WebSocketConfigurer {

    private RtcHandler rtcHandler = new RtcHandler();

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(rtcHandler, GlobalConstants.SOCKET_URL);
    }
}
