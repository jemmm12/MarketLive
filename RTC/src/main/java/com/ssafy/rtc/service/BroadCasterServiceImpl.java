package com.ssafy.rtc.service;

import com.ssafy.rtc.dto.RoomDto;
import com.ssafy.rtc.util.GlobalConstants;
import com.ssafy.rtc.util.GlobalFunctions;
import com.ssafy.rtc.video.RoomConnection;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class BroadCasterServiceImpl implements BroadCasterService {

    private final StringRedisTemplate redisTemplate;
    private final ConcurrentHashMap<Long, RoomConnection> roomSession;

    @Override
    public RoomDto getRoom(long userid) {
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        RoomDto roomDto = GlobalFunctions.getRoomDto(userid, hashOperations);
        return roomDto;
    }

    @Override
    public void createRoom(RoomDto roomDto) {
        // TODO: 사진 저장 추가

        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        String KEY = GlobalFunctions.generateRoomInfoKey(roomDto.getUserid());
        hashOperations.putAll(KEY, roomDtoToMap(roomDto));
        makeConnection(roomDto.getUserid());
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
        blowConnection(userid);
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

    private void makeConnection(Long broadcaster_id){
        roomSession.put(broadcaster_id, new RoomConnection(broadcaster_id));
    }

    private void blowConnection(Long broadcaster_id){
        roomSession.remove(broadcaster_id);
    }

    @Override
    public String getConnection(Long broadcaster_id){

        return  " , ";
    }
}
