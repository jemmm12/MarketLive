package com.ssafy.rtc.controller;

import com.ssafy.rtc.dto.RoomDto;
import com.ssafy.rtc.service.BroadCasterServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/broad")
@RequiredArgsConstructor
@Api("BraodCaster 컨트롤러 API ver 1.0")
public class BroadCasterController {

    private final BroadCasterServiceImpl broadCasterService;

    @ApiOperation(value = "방 정보 가져오기", notes = "해당 BroadCaster의 방 정보를 가져온다.", response = RoomDto.class)
    @GetMapping("/get-room/{userid}")
    public ResponseEntity<RoomDto> getRoom(@PathVariable("userid") @ApiParam(value = "방 정보를 얻어올 방의 user id", required = true) String userid) {
        RoomDto roomDto = broadCasterService.getRoom(userid);
        return new ResponseEntity<RoomDto>(roomDto, HttpStatus.CREATED);
    }

    @ApiOperation(value = "방 생성", notes = "BroadCaster가 방을 새로 만든다.", response = String.class)
    @PostMapping("/create-room")
    public ResponseEntity<String> createRoom(@RequestBody @ApiParam(value = "방을 만들기 위한 방 정보", required = true) RoomDto roomDto) {
        try{
            broadCasterService.createModifyRoom(roomDto);
        }catch(Exception e){
            return new ResponseEntity<>("create room failed", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("create room", HttpStatus.CREATED);
    }

    @ApiOperation(value = "방 수정", notes = "BroadCaster가 방 정보를 수정한다.", response = String.class)
    @PutMapping("/modify-room")
    public ResponseEntity<String> modifyRoom(@RequestBody @ApiParam(value = "방을 수정하기 위한 방 정보", required = true) RoomDto roomDto) {
        try{
            broadCasterService.createModifyRoom(roomDto);
        }catch(Exception e){
            return new ResponseEntity<>("modify room failed", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("modify room", HttpStatus.CREATED);
    }

    @ApiOperation(value = "방 폭파", notes = "BroadCaster가 방을 없앤다.", response = String.class)
    @DeleteMapping("/blow-room")
    public ResponseEntity<String> blowRoom(@RequestParam @ApiParam(value = "폭파할 방의 user id", required = true) String userid) {
        try{
            broadCasterService.blowRoom(userid);
        }catch(Exception e){
            return new ResponseEntity<>("blow room failed", HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>("blow room", HttpStatus.CREATED);
    }
}
