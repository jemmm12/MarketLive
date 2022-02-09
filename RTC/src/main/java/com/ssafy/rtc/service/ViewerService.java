package com.ssafy.rtc.service;

import com.ssafy.rtc.dto.RoomDto;

public interface ViewerService {

    RoomDto enterRoom(String broad_userid, String viewer_userid);
    void exitRoom(String broad_userid, String viewer_userid);

}
