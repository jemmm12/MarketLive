package com.ssafy.rtc.controller;

import com.ssafy.rtc.dto.RoomDto;
import com.ssafy.rtc.service.ViewerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/viewer")
@RequiredArgsConstructor
public class ViewerController {

    private final ViewerService viewerService;

    @PostMapping("/enter-room")
    public String enterRoom(@RequestBody RoomDto roomDto) {
        return "enter room";
    }

    @PostMapping("/exit-room")
    public String exitRoom(@RequestBody RoomDto roomDto) {
        return "exit room";
    }

}
