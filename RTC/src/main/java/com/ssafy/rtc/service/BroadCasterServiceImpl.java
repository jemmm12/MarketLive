package com.ssafy.rtc.service;

import com.ssafy.rtc.dto.RoomDto;
import com.ssafy.rtc.util.GlobalConstants;
import com.ssafy.rtc.util.GlobalFunctions;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BroadCasterServiceImpl implements BroadCasterService {

    private final StringRedisTemplate redisTemplate;

    @Override
    public RoomDto getRoom(String userid) {
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        RoomDto roomDto = GlobalFunctions.getRoomDto(userid, hashOperations);
        return roomDto;
    }

    @Override
    public void createModifyRoom(RoomDto roomDto) {
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        String KEY = GlobalFunctions.generateRoomInfoKey(roomDto.getUserid());
        hashOperations.putAll(KEY, roomDtoToMap(roomDto));
    }

    @Override
    public void blowRoom(String userid) {
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        String KEY = GlobalFunctions.generateRoomInfoKey(userid);
        redisTemplate.delete(KEY);
    }

    private Map<String, Object> roomDtoToMap(RoomDto roomDto) {
        Map<String, Object> map = new HashMap<>();
        map.put(GlobalConstants.ROOMDTO_TITLE, roomDto.getTitle());
        map.put(GlobalConstants.ROOMDTO_CATEGORY, roomDto.getCategory());
        map.put(GlobalConstants.ROOMDTO_INTRODUCE, roomDto.getIntroduce());
        map.put(GlobalConstants.ROOMDTO_STARTTIME, roomDto.getStarttime());
        map.put(GlobalConstants.ROOMDTO_ENDTIME, roomDto.getEndtime());
        map.put(GlobalConstants.ROOMDTO_THUMBNAIL, roomDto.getThumbnail());
        return map;
    }
}
