package com.ssafy.auth.controller;

import com.ssafy.auth.dto.LoginDto;
import com.ssafy.auth.dto.SignupDto;
import com.ssafy.auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<String> signupUser(@RequestBody SignupDto signupDto) {
        try{
            userService.saveUser(signupDto);
        }
        catch (Exception e) {
            return new ResponseEntity<>("회원가입에 실패했습니다.", HttpStatus.CREATED);
        }
        return new ResponseEntity<>("회원가입에 성공했습니다.", HttpStatus.CREATED);
    }

    @PostMapping("/signin")
    public ResponseEntity<String> signinUser(@RequestBody LoginDto loginDto){
        HttpHeaders responseHttpHeaders = new HttpHeaders();
        responseHttpHeaders.set("Authorization", "Bearer " + userService.loginUser(loginDto));

        if(userService.loginUser(loginDto) == null){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(userService.loginUser(loginDto), responseHttpHeaders, HttpStatus.OK);
    }

    @DeleteMapping(path = "/{email}")
    public ResponseEntity<Void> deleteUser(@PathVariable(name = "email") String email) {
        try {
            userService.deleteUser(email);
        }
        catch (Exception e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
