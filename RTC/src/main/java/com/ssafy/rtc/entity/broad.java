package com.ssafy.rtc.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;

@Getter
@Setter
@RedisHash("broad")
public class broad {

    private long userid;

    private String broadtitle;

    private String broadcategory;

    private String broadintroduce;

    private String broadstime;

    private String broadetime;

    private String broadthumbnail;


}
