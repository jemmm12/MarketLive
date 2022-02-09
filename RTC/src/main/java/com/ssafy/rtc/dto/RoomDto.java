package com.ssafy.rtc.dto;

import lombok.Data;

@Data
public class RoomDto {
    private String userid;
    private String title;
    private String category;
    private String introduce;
    private String starttime;
    private String endtime;
    private String thumbnail;
}
