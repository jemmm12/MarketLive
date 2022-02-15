package com.ssafy.rtc.video;

import com.google.gson.JsonObject;
import com.ssafy.rtc.util.ResponseKeys;
import org.kurento.client.KurentoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class RoomManager {
    private final Logger log = LoggerFactory.getLogger(RoomManager.class);

    @Resource(name="kurentoClient")
    private KurentoClient kurento;

    private final ConcurrentMap<String, Room> roomsByBid = new ConcurrentHashMap<>();    // broadcaster's id, room
    private final ConcurrentMap<String, Room> roomsBySession = new ConcurrentHashMap<>();   // broadcaster's sessionid, room
    private final ConcurrentMap<String, String> broadCasterByViewer = new ConcurrentHashMap<>();    // viewer's session id, broadcaster's id

    public void makeRoom(String broadCasterUserId, JsonObject jsonMessage, WebSocketSession session) throws IOException {
        if(roomsByBid.containsKey(broadCasterUserId)){
            handleErrorResponse(null, "Another user is currently acting as sender. Try again later ...", session, "presenterResponse");
            return;
        }
        try {
            Room room = new Room(broadCasterUserId, kurento.createMediaPipeline());
            room.initRoom(jsonMessage, session);
            roomsByBid.put(broadCasterUserId, room);
            roomsBySession.put(session.getId(), room);
        }catch(Throwable t){
            handleErrorResponse(t, null, session, "presenterResponse");
            return;
        }
    }

    public void enterRoom(String broadCasterUserId, JsonObject jsonMessage, WebSocketSession session) throws IOException{
        try{
            Room room = roomsByBid.get(broadCasterUserId);
            room.enterRoom(jsonMessage, session);
            broadCasterByViewer.put(session.getId(), broadCasterUserId);
        }catch(Throwable t){
            handleErrorResponse(t, "", session, "viewerResponse");
        }
    }

    public void iceCandidate(String broadCasterUserId, JsonObject jsonMessage, WebSocketSession session) {
        Room room = roomsByBid.get(broadCasterUserId);
        room.iceCandidate(jsonMessage, session);
    }

    public void stop(WebSocketSession session) throws IOException{
        String broadCasterUserId = "";
        Room room;
        if(broadCasterByViewer.containsKey(session.getId())){   // 현재 세션은 viewer
            broadCasterUserId = broadCasterByViewer.get(session.getId());
            room = roomsByBid.get(broadCasterUserId);
        }else{  //현재 세션은 broadcaster
            room = roomsBySession.get(session.getId());
        }
        try{
            switch(room.stop(session)){
                case "broadcaster":
                    roomsByBid.remove(broadCasterUserId);
                    roomsBySession.remove(session.getId());
                    Collection<String> strs = broadCasterByViewer.values();
                    for(String viewer_id : strs){
                        if(broadCasterByViewer.get(viewer_id).equals(broadCasterUserId)){
                            broadCasterByViewer.remove(viewer_id);
                        }
                    }
                    break;
                case "error":
                    throw new IOException();
                default:    // viewer
                    broadCasterByViewer.remove(session.getId());
                    break;
            }
        }catch(Throwable t){
            handleErrorResponse(t, "", session, "stopResponse");
        }
    }

    private void handleErrorResponse(Throwable throwable, String message, WebSocketSession session, String responseId)
            throws IOException {
        //stop(session);
        log.error(throwable.getMessage(), throwable);
        JsonObject response = new JsonObject();
        response.addProperty(ResponseKeys.ID.toString(), responseId);
        response.addProperty(ResponseKeys.RESPONSE.toString(), "rejected");
        if(throwable != null){
            response.addProperty(ResponseKeys.MESSAGE.toString(), throwable.getMessage());
        }else {
            response.addProperty(ResponseKeys.MESSAGE.toString(), message);
        }
        session.sendMessage(new TextMessage(response.toString()));
    }
}