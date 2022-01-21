package com.ssafy.auth.dto;

import com.ssafy.auth.entity.Authority;
import com.ssafy.auth.entity.User;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class SignupDto {
    private String email;
    private String password;
    private String phone;
    private String name;
    private String nickname;
    public User toEntity(){
        Set<Authority> authorities = new HashSet<>();
        authorities.add(Authority.USER);
        return User.builder()
                .name(name)
                .password(password)
                .phone(phone)
                .email(email)
                .authority(authorities)
                .build();
    }
}
