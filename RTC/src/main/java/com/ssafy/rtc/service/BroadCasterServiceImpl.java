package com.ssafy.rtc.service;

import com.ssafy.rtc.dto.RoomDto;
import com.ssafy.rtc.util.GlobalConstants;
import com.ssafy.rtc.util.GlobalFunctions;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BroadCasterServiceImpl implements BroadCasterService {

    private final StringRedisTemplate redisTemplate;

    @Override
    public RoomDto getRoom(long userid) {
        HashOperations<String, Object, Object> hashOperations = redisTemplate.opsForHash();
        RoomDto roomDto = GlobalFunctions.getRoomDto(userid, hashOperations);
        return roomDto;
    }

    @Override
    public void createRoom(RoomDto roomDto) {
        // TODO: 사진 저장 추가
//        String uploadUrl = "D:/thumbnailTest";
//        roomDto.setThumbnail(filePath);
//        UUID uuid = UUID.randomUUID();
//        String imgFileName = uuid + "_" + files.getOriginalFilename();
//        Path imageFilePath = Paths.get(uploadUrl + imgFileName);

//        try {
//            Files.write(imageFilePath, files.getBytes());
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }
//        roomDto.setThumbnail(imgFileName);

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
        map.put(GlobalConstants.ROOMDTO_CATEGORY, roomDto.getCategory());
        map.put(GlobalConstants.ROOMDTO_INTRODUCE, roomDto.getIntroduce());
        map.put(GlobalConstants.ROOMDTO_STARTTIME, roomDto.getStarttime());
        map.put(GlobalConstants.ROOMDTO_ENDTIME, roomDto.getEndtime());
        map.put(GlobalConstants.ROOMDTO_THUMBNAIL, roomDto.getThumbnail());
        return map;
    }
}
