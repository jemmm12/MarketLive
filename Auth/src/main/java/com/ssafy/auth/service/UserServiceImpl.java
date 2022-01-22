package com.ssafy.auth.service;

import com.ssafy.auth.dto.LoginDto;
import com.ssafy.auth.dto.SignupDto;
import com.ssafy.auth.dto.UpdateDto;
import com.ssafy.auth.dto.UserPageDto;
import com.ssafy.auth.entity.Authority;
import com.ssafy.auth.entity.User;
import com.ssafy.auth.repository.UserRepository;
import com.ssafy.auth.token.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public String saveUser(SignupDto signupDto) {
        if(!userRepository.existsByEmail(signupDto.getEmail())) {
            signupDto.setPassword(passwordEncoder.encode(signupDto.getPassword()));
            try {
                Set<Authority> authorities = new HashSet<>();
                authorities.add(Authority.USER);
                User user = new User().builder()
                        .email(signupDto.getEmail())
                        .password(signupDto.getPassword())
                        .phone(signupDto.getPhone())
                        .name(signupDto.getName())
                        .nickname(signupDto.getNickname())
                        .authority(authorities)
                        .build();
                userRepository.save(user);
            }
            catch (Exception e) {
                throw e;
            }
        }
        return "Success";
    }

    @Override
    public String loginUser(LoginDto loginDto) {
        User user = userRepository.findByEmail(loginDto.getEmail()).get();
        if(passwordEncoder.matches(loginDto.getPassword(), user.getPassword())){ // credential 은 비밀번호 부분이라 그냥 없는채로 넣는다
            return jwtTokenProvider.createToken(new UsernamePasswordAuthenticationToken(user.getUserid(),"",user.getAuthority()));
        }else{
            return null;
        }
    }

    @Override
    public UserPageDto browseUser(String nickname) {
        User user = userRepository.findByNickname(nickname).get();
        if(user == null) return null;
        return new UserPageDto().builder()
                                .userId(user.getUserid())
                                .email(user.getEmail())
                                .nickname(user.getNickname())
                                .oneline(user.getOneline())
                                .build();
    }

    @Override
    public void deleteUser(Long userid, String token) throws Exception {
        if(jwtTokenProvider.vaildateToken(token)) {
            User user = userRepository.findByUserid(userid).get();
            userRepository.delete(user);
        }
    }

    @Override
    public void updateUser(Long userid, String token, UpdateDto updateDto) throws Exception {
        if(jwtTokenProvider.vaildateToken(token)) {
            User user = userRepository.findByUserid(userid).get();
            user.setNickname(updateDto.getNickname());
            user.setOneline(updateDto.getOneline());
            userRepository.save(user);
        }
    }

    @Override 
    public boolean isduplicatedEmail(String email) { // 이메일 중복체크
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean isduplicatedNickname(String nickname) { // 닉네임 중복체크
        return userRepository.existsByNickname(nickname);
    }
}
