package com.ssafy.auth.service;

import com.ssafy.auth.dto.*;
import org.springframework.web.multipart.MultipartFile;

public interface UserService {
    String saveUser(SignupDto signupDto);
    String loginUser(LoginDto loginDto);
    UserPageDto browseUser(String nickname);
    MyPageDto mypageUser(String token) throws Exception;
    void deleteUser(Long userid, String token) throws Exception;
    void updateUser(Long userid, String token, UpdateDto updateDto) throws Exception;
    void updateThumbnail(Long userid, MultipartFile multipartFile) throws Exception;
    boolean isduplicatedEmail(String email);
    boolean isduplicatedNickname(String nickname);
}
