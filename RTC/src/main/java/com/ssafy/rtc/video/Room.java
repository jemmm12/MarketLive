package com.ssafy.rtc.video;

import com.google.gson.JsonObject;
import org.kurento.client.IceCandidate;
import org.kurento.client.MediaPipeline;
import org.kurento.client.WebRtcEndpoint;
import org.kurento.jsonrpc.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class Room {
    private final Logger log = LoggerFactory.getLogger(Room.class);

    private MediaPipeline pipeline;
    private final ConcurrentHashMap<String, UserSession> viewers = new ConcurrentHashMap<>();
    private UserSession broadCaster;
    private final String broadCasterUserId;

    public Room(String broadCasterUserId, MediaPipeline pipeline) {
        this.broadCasterUserId = broadCasterUserId;
        this.pipeline = pipeline;
        log.info("id : {} 의 방이 생성되었습니다", broadCasterUserId);
    }

    public String getBroadCasterUserId() {
        return broadCasterUserId;
    }

    public synchronized void initRoom(JsonObject jsonMessage, WebSocketSession session) throws IOException {
        broadCaster = new UserSession(session);
        broadCaster.setWebRtcEndpoint(new WebRtcEndpoint.Builder(pipeline).build());
        WebRtcEndpoint broadCasterWebRtc = broadCaster.getWebRtcEndpoint();
        broadCasterWebRtc.addIceCandidateFoundListener(event -> {
            JsonObject response = new JsonObject();
            response.addProperty("id", "iceCandidate");
            response.add("candidate", JsonUtils.toJsonObject(event.getCandidate()));
            try {
                synchronized (session) {
                    session.sendMessage(new TextMessage(response.toString()));
                }
            } catch (IOException e) {
                log.debug(e.getMessage());
            }
        });
        String sdpOffer = jsonMessage.getAsJsonPrimitive("sdpOffer").getAsString();
        String sdpAnswer = broadCasterWebRtc.processOffer(sdpOffer);

        JsonObject response = new JsonObject();
        response.addProperty("id", "presenterResponse");
        response.addProperty("response", "accepted");
        response.addProperty("sdpAnswer", sdpAnswer);

        synchronized (session) {
            broadCaster.sendMessage(response);
        }
        broadCasterWebRtc.gatherCandidates();
    }

    public synchronized void enterRoom(JsonObject jsonMessage, WebSocketSession session) throws IOException {
        UserSession viewer = new UserSession(session);
        viewers.put(session.getId(), viewer);
        WebRtcEndpoint nextWebRtc = new WebRtcEndpoint.Builder(pipeline).build();
        nextWebRtc.addIceCandidateFoundListener(event -> {
            JsonObject response = new JsonObject();
            response.addProperty("id", "iceCandidate");
            response.add("candidate", JsonUtils.toJsonObject(event.getCandidate()));
            try {
                synchronized (session) {
                    session.sendMessage(new TextMessage(response.toString()));
                }
            } catch (IOException e) {
                log.debug(e.getMessage());
            }
        });
        viewer.setWebRtcEndpoint(nextWebRtc);
        broadCaster.getWebRtcEndpoint().connect(nextWebRtc);
        String sdpOffer = jsonMessage.getAsJsonPrimitive("sdpOffer").getAsString();
        String sdpAnswer = nextWebRtc.processOffer(sdpOffer);

        JsonObject response = new JsonObject();
        response.addProperty("id", "viewerResponse");
        response.addProperty("response", "accepted");
        response.addProperty("sdpAnswer", sdpAnswer);

        synchronized (session) {
            viewer.sendMessage(response);
        }
        nextWebRtc.gatherCandidates();
    }

    public void iceCandidate(JsonObject jsonMessage, WebSocketSession session) {
        JsonObject candidate = jsonMessage.get("candidate").getAsJsonObject();

        UserSession user = null;
        if (broadCaster != null) {
            if (broadCaster.getSession() == session) {
                user = broadCaster;
            } else {
                user = viewers.get(session.getId());
            }
        }
        if (user != null) {
            IceCandidate cand =
                    new IceCandidate(candidate.get("candidate").getAsString(), candidate.get("sdpMid")
                            .getAsString(), candidate.get("sdpMLineIndex").getAsInt());
            user.addCandidate(cand);
        }
    }

    public synchronized String stopRoom(JsonObject jsonMessage, WebSocketSession session) throws IOException {
        String sessionId = session.getId();
        if (broadCaster != null && broadCaster.getSession().getId().equals(sessionId)) {
            for (UserSession viewer : viewers.values()) {
                JsonObject response = new JsonObject();
                response.addProperty("id", "stopCommunication");
                viewer.sendMessage(response);
            }

            log.info("Releasing media pipeline");
            if (pipeline != null) {
                pipeline.release();
            }
            pipeline = null;
            broadCaster = null;
            return "broadcaster";
        } else if (viewers.containsKey(sessionId)) {
            if (viewers.get(sessionId).getWebRtcEndpoint() != null) {
                viewers.get(sessionId).getWebRtcEndpoint().release();
            }
            viewers.remove(sessionId);
            return "viewer";
        }
        return "error";
    }


}
