package com.ssafy.rtc.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TestService {

    @Autowired
    StringRedisTemplate redisTemplate;

    public void addMember() {
        final String key = "testSort1";
        ZSetOperations<String, String> stringStringZSetOperations = redisTemplate.opsForZSet();

        stringStringZSetOperations.add(key, "broad_id1", 5);
        stringStringZSetOperations.add(key, "broad_id2", 3);
        stringStringZSetOperations.add(key, "broad_id3", 4);


    }

}
