package com.ssafy.dm.controller;

import com.ssafy.dm.dto.DmDto;
import com.ssafy.dm.entity.DmEntity;
import com.ssafy.dm.service.DmService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/dm")
@RequiredArgsConstructor
public class DmController {

    private final DmService dmService;

    @PostMapping("/creat") // 메세지 만들기 완료
    public Map<String, Object> creatDmMessage(@RequestBody DmDto dmDto) {
        Map<String, Object> response = new HashMap<>();

        DmEntity dm = dmService.sendDm(dmDto);
        if(dm != null) {
            response.put("result", "SUCCESS");
            response.put("Content-type", "application/json;charset=UTF-8");
            response.put("Accept", "application/json");
            response.put("dm", dm);
        } else {
            response.put("result", "FAIL");
            response.put("reason", "메세지 보내기 실패");
        }

        return response;
    }

    @GetMapping("/{id}") // 메세지 하나 확인(dm_id), 완료
    public Map<String, Object> findById(@PathVariable("id") long id) {
        Map<String, Object> response = new HashMap<>();

        Optional<DmEntity> dm = dmService.findDm(id);
        if(dm != null) {
            response.put("result", "SUCCESS");
            response.put("dm", dm);
        } else {
            response.put("result", "FAIL");
            response.put("reason", "메세지가 없습니다.");
        }

        return response;
    }

    @GetMapping("/all/{id}") // 메세지 전체 조회(receiver_id), 완료
    public ResponseEntity<List<DmEntity>> findAllByfReceiverId(@PathVariable("id") Long id) {
        List<DmEntity> dm = dmService.findAllDm(id);

        if(dm != null)
            return new ResponseEntity<>(dm, HttpStatus.OK);
        else
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @DeleteMapping("/{id}") // 메세지 삭제(dm_id), 완료
    public Map<String, Object> deleteDm(@PathVariable("id") long id) {
        Map<String, Object> response = new HashMap<>();

        if(dmService.deleteDm(id) > 0) {
            response.put("result", "메세지 삭제 완료");
        }
        else {
            response.put("result", "FAIL");
            response.put("reason", "삭제 할 수 없는 메세지 입니다.");
        }

        return response;
    }
}
