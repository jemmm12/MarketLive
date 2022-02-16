package com.ssafy.rtc.service;

import com.ssafy.rtc.dto.RoomDto;
import org.springframework.web.multipart.MultipartFile;

public interface BroadCasterService {

    void createRoom(RoomDto roomDto) throws Exception;
    void modifyRoom(RoomDto roomDto) throws Exception;
    //void blowRoom(long userid) throws Exception;
}
