package com.ssafy.rtc.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResponseValues {
    MAKEROOM("makeRoom"),
    ENTERROOM("enterRoom"),
    STOP("stop"),
    ERROR("error");

    private final String value;

    public static ResponseValues findByValues(String value){
        for(ResponseValues val : ResponseValues.values()){
            if(val.equals(value)){
                return val;
            }
        }
        return ERROR;
    }

    @Override
    public String toString(){
        return value;
    }
}
