package com.ssafy.rtc.video;

import com.google.gson.JsonObject;
import com.ssafy.rtc.util.GlobalFunctions;
import com.ssafy.rtc.util.ResponseKeys;
import org.kurento.client.KurentoClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class RoomManager {
    private final Logger log = LoggerFactory.getLogger(RoomManager.class);

    @Resource(name = "kurentoClient")
    private KurentoClient kurento;

    @Autowired
    private StringRedisTemplate redisTemplate;

    private final ConcurrentMap<String, Room> roomsByBid = new ConcurrentHashMap<>();    // broadcaster's id, room
    private final ConcurrentMap<String, Room> roomsBySession = new ConcurrentHashMap<>();   // broadcaster's sessionid, room
    private final ConcurrentMap<String, String> broadIdByBroadSid = new ConcurrentHashMap<>();  // (broadcaster's session id, broad id)
    private final ConcurrentMap<String, String> broadViewerIdByViewerSid = new ConcurrentHashMap<>();   // (viewer's session id, broad + viewer's id)

    public void makeRoom(String broadCasterUserId, JsonObject jsonMessage, WebSocketSession session) throws IOException {
        if (roomsByBid.containsKey(broadCasterUserId)) {
            handleErrorResponse(null, "Another user is currently acting as sender. Try again later ...", session, "presenterResponse");
            return;
        }
        try {
            Room room = new Room(broadCasterUserId, kurento.createMediaPipeline());
            room.initRoom(jsonMessage, session);
            roomsByBid.put(broadCasterUserId, room);
            roomsBySession.put(session.getId(), room);
            broadIdByBroadSid.put(session.getId(), broadCasterUserId);
        } catch (Throwable t) {
            handleErrorResponse(t, null, session, "presenterResponse");
            return;
        }
    }

    public void enterRoom(String broadCasterUserId, JsonObject jsonMessage, WebSocketSession session) throws IOException {
        try {
            Room room = roomsByBid.get(broadCasterUserId);
            room.enterRoom(jsonMessage, session);
            String viewerUserId = jsonMessage.get(ResponseKeys.USERID.toString()).getAsString();
            broadViewerIdByViewerSid.put(session.getId(), broadCasterUserId + ":" + viewerUserId);
        } catch (Throwable t) {
            handleErrorResponse(t, "", session, "viewerResponse");
        }
    }

    public void iceCandidate(String broadCasterUserId, JsonObject jsonMessage, WebSocketSession session) {
        Room room = roomsByBid.get(broadCasterUserId);
        room.iceCandidate(jsonMessage, session);
    }

    public void sendMessage(String broadCasterUserId, JsonObject jsonMessage, WebSocketSession session) throws IOException {
        try{
            Room room = roomsByBid.get(broadCasterUserId);
            room.sendMessage(jsonMessage, session);
        }catch (Throwable t) {
            handleErrorResponse(t, "", session, "viewerResponse");
        }
    }

    public void stop(WebSocketSession session) throws IOException {
        String broadCasterUserId = "";
        String viewerUserId = null;
        Room room;
        if (broadViewerIdByViewerSid.containsKey(session.getId())) {   // 현재 세션은 viewer
            String[] ids = broadViewerIdByViewerSid.get(session.getId()).split(":");
            broadCasterUserId = ids[0];
            viewerUserId = ids[1];
            room = roomsByBid.get(broadCasterUserId);
        } else {  //현재 세션은 broadcaster
            broadCasterUserId = broadIdByBroadSid.get(session.getId());  //broadcaster id 안 가지고 오는 버그 수정
            room = roomsBySession.get(session.getId());
        }

        // redis에서 정보 삭제하기
        deleteRedisInfo(broadCasterUserId, viewerUserId);

        // 방 없애거나 viewer 정보 없애기
        deleteRoomInfo(room, session, broadCasterUserId);
    }

    private void deleteRedisInfo(String broadCasterUserId, String viewerUserId) {
        SetOperations<String, String> setOperations = redisTemplate.opsForSet();
        String VIEWERS_KEY = GlobalFunctions.generateRoomViewersKey(Long.parseLong(broadCasterUserId));
        if (viewerUserId != null) {   // viewer
            if(setOperations.isMember(VIEWERS_KEY, viewerUserId))
            {
                setOperations.remove(VIEWERS_KEY, viewerUserId);
            }
        } else {  //broadcaster
            // set 삭제
            if(redisTemplate.hasKey(VIEWERS_KEY)) {
                redisTemplate.delete(VIEWERS_KEY);
            }
            // room 삭제
            String ROOMINFO_KEY = GlobalFunctions.generateRoomInfoKey(Long.parseLong(broadCasterUserId));
            if(redisTemplate.hasKey(ROOMINFO_KEY)) {
                redisTemplate.delete(ROOMINFO_KEY);
            }
        }
    }

    private void deleteRoomInfo(Room room, WebSocketSession session, String broadCasterUserId) throws IOException {
        try {
            switch (room.stop(session)) {
                case "broadcaster":
                    roomsByBid.remove(broadCasterUserId);
                    roomsBySession.remove(session.getId());
                    broadIdByBroadSid.remove(session.getId());
                    // 모든 viewer 정보 삭제
                    Collection<String> strs = broadViewerIdByViewerSid.values();
                    for (String viewer_id : strs) {
                        if (broadViewerIdByViewerSid.get(viewer_id).split(":")[0].equals(broadCasterUserId)) {
                            broadViewerIdByViewerSid.remove(viewer_id);
                        }
                    }
                    break;
                case "error":
                    throw new IOException();
                default:    // viewer
                    broadViewerIdByViewerSid.remove(session.getId());
                    break;
            }
        } catch (Throwable t) {
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
        if (throwable != null) {
            response.addProperty(ResponseKeys.MESSAGE.toString(), throwable.getMessage());
        } else {
            response.addProperty(ResponseKeys.MESSAGE.toString(), message);
        }
        session.sendMessage(new TextMessage(response.toString()));
    }


}
