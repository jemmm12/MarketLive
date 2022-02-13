package com.ssafy.rtc.service;

import com.ssafy.rtc.dto.RoomDto;

public interface BroadCasterService {

    RoomDto getRoom(long userid);
    void createRoom(RoomDto roomDto) throws Exception;
    void modifyRoom(RoomDto roomDto) throws Exception;
    void blowRoom(long userid) throws Exception;

    String getConnection(Long broadcaster_id);
}
