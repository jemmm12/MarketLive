package com.ssafy.auth.service;

import com.ssafy.auth.dto.LoginDto;
import com.ssafy.auth.dto.SignupDto;
import com.ssafy.auth.dto.UpdateDto;
import com.ssafy.auth.dto.UserPageDto;

public interface UserService {
    String saveUser(SignupDto signupDto);
    String loginUser(LoginDto loginDto);
    UserPageDto browseUser(String nickname);
    void deleteUser(Long userid, String token) throws Exception;
    void updateUser(Long userid, String token, UpdateDto updateDto) throws Exception;
    boolean isduplicatedEmail(String email);
    boolean isduplicatedNickname(String nickname);
}
