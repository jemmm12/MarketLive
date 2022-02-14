package com.ssafy.rtc.video;

import com.google.gson.JsonObject;
import org.kurento.client.KurentoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class RoomManager {
    private final Logger log = LoggerFactory.getLogger(RoomManager.class);

    @Resource(name="kurentoClient")
    private KurentoClient kurento;

    private final ConcurrentMap<String, Room> rooms = new ConcurrentHashMap<>();    // broad_id, room

    public void makeRoom(String broadCasterUserId, JsonObject jsonMessage, WebSocketSession session) throws IOException {
        Room room = new Room(broadCasterUserId, kurento.createMediaPipeline());
        try {
            room.initRoom(jsonMessage, session);
        }catch(Throwable t){
            handleErrorResponse(t, session, "presenterResponse");
        }
        rooms.put(broadCasterUserId, room);
    }

    public void removeRoom(Room room){
        this.rooms.remove(room.getBroadCasterUserId());
        //room.close();
    }

    private void handleErrorResponse(Throwable throwable, WebSocketSession session, String responseId)
            throws IOException {
        //stop(session);
        log.error(throwable.getMessage(), throwable);
        JsonObject response = new JsonObject();
        response.addProperty("id", responseId);
        response.addProperty("response", "rejected");
        response.addProperty("message", throwable.getMessage());
        session.sendMessage(new TextMessage(response.toString()));
    }

    public void enterRoom(String broadCasterUserId, JsonObject jsonMessage, WebSocketSession session) throws IOException{
        Room room = rooms.get(broadCasterUserId);
        try{
            room.enterRoom(jsonMessage, session);
        }catch(Throwable t){
            handleErrorResponse(t, session, "viewerResponse");
        }
    }

    public void stopRoom(String broadCasterUserId, JsonObject jsonMessage, WebSocketSession session) throws IOException{
        Room room = rooms.get(broadCasterUserId);
        try{
            switch(room.stopRoom(jsonMessage, session)){
                case "broadcaster":
                    rooms.remove(broadCasterUserId);
                    break;
                case "error":
                    throw new IOException();
                default:
                    break;
            }
        }catch(Throwable t){
            handleErrorResponse(t, session, "stopResponse");
        }
    }
}
