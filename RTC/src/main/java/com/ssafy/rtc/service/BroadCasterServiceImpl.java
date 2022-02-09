package com.ssafy.rtc.service;

import com.ssafy.rtc.dto.RoomDto;
import com.ssafy.rtc.util.constants;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BroadCasterServiceImpl implements BroadCasterService {

    private final StringRedisTemplate redisTemplate;

    @Override
    public RoomDto getRoom(String userid) {
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        String KEY = constants.generateRoomInfoKey(userid);
        RoomDto roomDto = getRoomDto(KEY, hashOperations);
        roomDto.setUserid(userid);
        return roomDto;
    }

    @Override
    public void createModifyRoom(RoomDto roomDto) {
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        String KEY = constants.generateRoomInfoKey(roomDto.getUserid());
        hashOperations.putAll(KEY, roomDtoToMap(roomDto));
    }

    @Override
    public void blowRoom(String userid) {
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        String KEY = constants.generateRoomInfoKey(userid);
        redisTemplate.delete(KEY);
    }

    private Map<String, Object> roomDtoToMap(RoomDto roomDto) {
        Map<String, Object> map = new HashMap<>();
        map.put(constants.ROOMDTO_TITLE, roomDto.getTitle());
        map.put(constants.ROOMDTO_CATEGORY, roomDto.getCategory());
        map.put(constants.ROOMDTO_INTRODUCE, roomDto.getIntroduce());
        map.put(constants.ROOMDTO_STARTTIME, roomDto.getStarttime());
        map.put(constants.ROOMDTO_ENDTIME, roomDto.getEndtime());
        map.put(constants.ROOMDTO_THUMBNAIL, roomDto.getThumbnail());
        return map;
    }

    private RoomDto getRoomDto(String key, HashOperations<String, Object, Object> hashOperations) {
        RoomDto roomDto = new RoomDto();
        roomDto.setTitle(hashOperations.get(key, constants.ROOMDTO_TITLE).toString());
        roomDto.setCategory(hashOperations.get(key, constants.ROOMDTO_CATEGORY).toString());
        roomDto.setIntroduce(hashOperations.get(key, constants.ROOMDTO_INTRODUCE).toString());
        roomDto.setStarttime(hashOperations.get(key, constants.ROOMDTO_STARTTIME).toString());
        roomDto.setEndtime(hashOperations.get(key, constants.ROOMDTO_ENDTIME).toString());
        roomDto.setThumbnail(hashOperations.get(key, constants.ROOMDTO_THUMBNAIL).toString());
        return roomDto;
    }
}
