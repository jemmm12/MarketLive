package com.ssafy.rtc.controller;

import com.ssafy.rtc.service.TestService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {
	//테스트용
	private final TestService testService;

	@GetMapping("/hello")
	public String hi() {
		return "PORT 8113 test";
	}

	@GetMapping("/test")
	public String test(){
		testService.addMember();
		return "redis test";
	}
}
