package com.ssafy.rtc.service;

import com.google.common.collect.Lists;
import com.ssafy.rtc.dto.RoomDto;
import com.ssafy.rtc.util.GlobalConstants;
import com.ssafy.rtc.util.GlobalFunctions;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BroadCasterServiceImpl implements BroadCasterService {

    private final StringRedisTemplate redisTemplate;

    @Override
    public void createRoom(RoomDto roomDto) {
        // TODO: 사진 저장 추가

        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        String KEY = GlobalFunctions.generateRoomInfoKey(roomDto.getUserid());
        hashOperations.putAll(KEY, roomDtoToMap(roomDto));
    }

    @Override
    public void modifyRoom(RoomDto roomDto) {
        // TODO: 사진 수정

        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        String KEY = GlobalFunctions.generateRoomInfoKey(roomDto.getUserid());
        hashOperations.putAll(KEY, roomDtoToMap(roomDto));
    }

    @Override
    public void blowRoom(long userid) {
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        String KEY = GlobalFunctions.generateRoomInfoKey(userid);
        redisTemplate.delete(KEY);
    }

    private Map<String, Object> roomDtoToMap(RoomDto roomDto) {
        Map<String, Object> map = new HashMap<>();
        map.put(GlobalConstants.ROOMDTO_TITLE, roomDto.getTitle());
        map.put(GlobalConstants.ROOMDTO_NICKNAME, roomDto.getNickname());
        map.put(GlobalConstants.ROOMDTO_CATEGORY, roomDto.getCategory());
        map.put(GlobalConstants.ROOMDTO_INTRODUCE, roomDto.getIntroduce());
        map.put(GlobalConstants.ROOMDTO_STARTTIME, roomDto.getStarttime());
        map.put(GlobalConstants.ROOMDTO_ENDTIME, roomDto.getEndtime());
        map.put(GlobalConstants.ROOMDTO_THUMBNAIL, roomDto.getThumbnail());
        return map;
    }
}
