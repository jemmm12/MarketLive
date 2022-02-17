package com.ssafy.rtc.service;

import com.ssafy.rtc.dto.RoomDto;
import org.springframework.web.multipart.MultipartFile;

public interface BroadCasterService {

    void createRoom(RoomDto roomDto, MultipartFile multipartFile) throws Exception;

    byte[] getThumbnail(Long userid) throws Exception;

    void modifyRoom(RoomDto roomDto) throws Exception;

}
