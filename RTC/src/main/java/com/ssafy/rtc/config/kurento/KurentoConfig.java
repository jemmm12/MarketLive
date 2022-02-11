package com.ssafy.rtc.config.kurento;

import com.ssafy.rtc.video.RoomConnection;
import org.kurento.client.KurentoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

import java.util.concurrent.ConcurrentHashMap;

@Configuration
@EnableWebSocket
public class KurentoConfig {

    @Bean
    public ConcurrentHashMap<Long, RoomConnection> roomSession() {
        return new ConcurrentHashMap<>();     // broadCasterId, connection
    }

    @Bean
    public KurentoClient kurentoClient() {
        return KurentoClient.create();
    }

}
