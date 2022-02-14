package com.ssafy.rtc.video;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.ssafy.rtc.util.ResponseKeys;
import com.ssafy.rtc.util.ResponseValues;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.io.IOException;

public class RtcHandler extends TextWebSocketHandler {

    private static final Logger log = LoggerFactory.getLogger(RtcHandler.class);
    private static final Gson gson = new GsonBuilder().create();

    @Autowired
    private RoomManager roomManager;

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        final JsonObject jsonMessage = gson.fromJson(message.getPayload(), JsonObject.class);

        switch (ResponseValues.findByValues(jsonMessage.get(ResponseKeys.ID.toString()).getAsString())) {
            case MAKEROOM:  //broadcaster
                makeRoom(jsonMessage, session);
                break;
            case ENTERROOM: //viewer
                enterRoom(jsonMessage, session);
                break;
            case ICECANDIDATE:
                candidate(jsonMessage, session);
                break;
            case STOP:
                stopRoom(jsonMessage, session);
            default:
                JsonObject response = new JsonObject();
                response.addProperty("id", "message failed");
                session.sendMessage(new TextMessage(response.toString()));
                break;
        }
    }

    private void makeRoom(JsonObject jsonMessage, WebSocketSession session) throws IOException {
        final String broadCasterUserId = jsonMessage.get(ResponseKeys.ROOMID.toString()).getAsString();
        roomManager.makeRoom(broadCasterUserId, jsonMessage, session);
    }

    private void enterRoom(JsonObject jsonMessage, WebSocketSession session) throws IOException {
        final String broadCasterUserId = jsonMessage.get(ResponseKeys.ROOMID.toString()).getAsString();
        roomManager.enterRoom(broadCasterUserId, jsonMessage, session);
    }

    private void candidate(JsonObject jsonMessage, WebSocketSession session) {
        final String broadCasterUserId = jsonMessage.get(ResponseKeys.ROOMID.toString()).getAsString();
        roomManager.iceCandidate(broadCasterUserId, jsonMessage, session);
    }

    private void stopRoom(JsonObject jsonMessage, WebSocketSession session) throws IOException {
        final String broadCasterUserId = jsonMessage.get(ResponseKeys.ROOMID.toString()).getAsString();
        roomManager.stopRoom(broadCasterUserId, jsonMessage, session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        //roomManager.stopRoom(session);
    }
}
