package com.ssafy.rtc.service;

import com.ssafy.rtc.dto.RoomDto;

public interface BroadCasterService {

    RoomDto getRoom(String userid);
    void createModifyRoom(RoomDto roomDto) throws Exception;
    void blowRoom(String userid) throws Exception;

}
