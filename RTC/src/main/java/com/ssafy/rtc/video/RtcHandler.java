package com.ssafy.rtc.video;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.ssafy.rtc.util.GlobalConstants;
import org.kurento.client.*;
import org.kurento.jsonrpc.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class RtcHandler extends TextWebSocketHandler {

    private static final Logger log = LoggerFactory.getLogger(RtcHandler.class);
    private static final Gson gson = new GsonBuilder().create();

    @Resource(name="kurentoClient")
    private KurentoClient kurento;

    private final ConcurrentHashMap<String, UserSession> viewers = new ConcurrentHashMap<>();
    private UserSession broadCaster;
    private MediaPipeline pipeline;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        JsonObject jsonMessage = gson.fromJson(message.getPayload(), JsonObject.class);
        switch (jsonMessage.get(GlobalConstants.PROPERTY_ID).getAsString()) {
            case "broadcaster":
                try {
                    presenter(session, jsonMessage);
                } catch (Throwable t) {
                    handleErrorResponse(t, session, GlobalConstants.VALUE_BROADCASTER);
                }
                break;
            case "viewer":
                try {
                    viewer(session, jsonMessage);
                } catch (Throwable t) {
                    handleErrorResponse(t, session, GlobalConstants.VALUE_VIEWER);
                }
                break;
            case "onIceCandidate":
                JsonObject candidate = jsonMessage.get(GlobalConstants.PROPERTY_CANDIDATE).getAsJsonObject();

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
                            new IceCandidate(candidate.get(GlobalConstants.PROPERTY_CANDIDATE).getAsString(), candidate.get("sdpMid")
                                    .getAsString(), candidate.get("sdpMLineIndex").getAsInt());
                    user.addCandidate(cand);
                }
                break;
            case "stop":
                stop(session);
                break;
            default:
                break;
        }

    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        stop(session);
    }

    // ------ methods -------

    private synchronized void presenter(final WebSocketSession session, JsonObject jsonMessage)
            throws IOException {
        if (broadCaster == null) {
            broadCaster = new UserSession(session);

            pipeline = kurento.createMediaPipeline();
            broadCaster.setWebRtcEndpoint(new WebRtcEndpoint.Builder(pipeline).build());

            WebRtcEndpoint presenterWebRtc = broadCaster.getWebRtcEndpoint();

            presenterWebRtc.addIceCandidateFoundListener(new EventListener<IceCandidateFoundEvent>() {

                @Override
                public void onEvent(IceCandidateFoundEvent event) {
                    JsonObject response = new JsonObject();
                    response.addProperty(GlobalConstants.PROPERTY_ID, GlobalConstants.VALUE_ICECANDIDATE);
                    response.add(GlobalConstants.PROPERTY_CANDIDATE, JsonUtils.toJsonObject(event.getCandidate()));
                    try {
                        synchronized (session) {
                            session.sendMessage(new TextMessage(response.toString()));
                        }
                    } catch (IOException e) {
                        log.debug(e.getMessage());
                    }
                }
            });

            String sdpOffer = jsonMessage.getAsJsonPrimitive("sdpOffer").getAsString();
            String sdpAnswer = presenterWebRtc.processOffer(sdpOffer);

            JsonObject response = new JsonObject();
            response.addProperty(GlobalConstants.PROPERTY_ID, GlobalConstants.VALUE_BROADCASTER);
            response.addProperty(GlobalConstants.PROPERTY_RESPONSE, GlobalConstants.VALUE_ACCEPTED);
            response.addProperty(GlobalConstants.PROPERTY_SDPANSWER, sdpAnswer);

            synchronized (session) {
                broadCaster.sendMessage(response);
            }
            presenterWebRtc.gatherCandidates();

        } else {
            JsonObject response = new JsonObject();
            response.addProperty(GlobalConstants.PROPERTY_ID, GlobalConstants.VALUE_BROADCASTER);
            response.addProperty(GlobalConstants.PROPERTY_RESPONSE, GlobalConstants.VALUE_REJECTED);
            response.addProperty(GlobalConstants.PROPERTY_MESSAGE,
                    "Another user is currently acting as sender. Try again later ...");
            session.sendMessage(new TextMessage(response.toString()));
        }
    }

    private synchronized void viewer(final WebSocketSession session, JsonObject jsonMessage)
            throws IOException {
        if (broadCaster == null || broadCaster.getWebRtcEndpoint() == null) {
            JsonObject response = new JsonObject();
            response.addProperty(GlobalConstants.PROPERTY_ID, GlobalConstants.VALUE_VIEWER);
            response.addProperty(GlobalConstants.PROPERTY_RESPONSE, GlobalConstants.VALUE_REJECTED);
            response.addProperty(GlobalConstants.PROPERTY_MESSAGE,
                    "No active sender now. Become sender or . Try again later ...");
            session.sendMessage(new TextMessage(response.toString()));
        } else {
            if (viewers.containsKey(session.getId())) {
                JsonObject response = new JsonObject();
                response.addProperty(GlobalConstants.PROPERTY_ID, GlobalConstants.VALUE_VIEWER);
                response.addProperty(GlobalConstants.PROPERTY_RESPONSE, GlobalConstants.VALUE_REJECTED);
                response.addProperty(GlobalConstants.PROPERTY_MESSAGE, "You are already viewing in this session. "
                        + "Use a different browser to add additional viewers.");
                session.sendMessage(new TextMessage(response.toString()));
                return;
            }
            UserSession viewer = new UserSession(session);
            viewers.put(session.getId(), viewer);

            WebRtcEndpoint nextWebRtc = new WebRtcEndpoint.Builder(pipeline).build();

            nextWebRtc.addIceCandidateFoundListener(event -> {
                JsonObject response = new JsonObject();
                response.addProperty(GlobalConstants.PROPERTY_ID, GlobalConstants.VALUE_ICECANDIDATE);
                response.add(GlobalConstants.PROPERTY_CANDIDATE, JsonUtils.toJsonObject(event.getCandidate()));
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
            response.addProperty(GlobalConstants.PROPERTY_ID, GlobalConstants.VALUE_VIEWER);
            response.addProperty(GlobalConstants.PROPERTY_RESPONSE, GlobalConstants.VALUE_ACCEPTED);
            response.addProperty(GlobalConstants.PROPERTY_SDPANSWER, sdpAnswer);

            synchronized (session) {
                viewer.sendMessage(response);
            }
            nextWebRtc.gatherCandidates();
        }
    }

    private void handleErrorResponse(Throwable throwable, WebSocketSession session, String responseId)
            throws IOException {
        stop(session);
        log.error(throwable.getMessage(), throwable);
        JsonObject response = new JsonObject();
        response.addProperty(GlobalConstants.PROPERTY_ID, responseId);
        response.addProperty(GlobalConstants.PROPERTY_RESPONSE, GlobalConstants.VALUE_REJECTED);
        response.addProperty(GlobalConstants.PROPERTY_MESSAGE, throwable.getMessage());
        session.sendMessage(new TextMessage(response.toString()));
    }

    private synchronized void stop(WebSocketSession session) throws IOException {
        String sessionId = session.getId();
        if (broadCaster != null && broadCaster.getSession().getId().equals(sessionId)) {
            for (UserSession viewer : viewers.values()) {
                JsonObject response = new JsonObject();
                response.addProperty(GlobalConstants.PROPERTY_ID, "stopCommunication");
                viewer.sendMessage(response);
            }

            log.info("Releasing media pipeline");
            if (pipeline != null) {
                pipeline.release();
            }
            pipeline = null;
            broadCaster = null;
        } else if (viewers.containsKey(sessionId)) {
            if (viewers.get(sessionId).getWebRtcEndpoint() != null) {
                viewers.get(sessionId).getWebRtcEndpoint().release();
            }
            viewers.remove(sessionId);
        }
    }
}
