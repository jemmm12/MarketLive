package com.ssafy.rtc;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {
	@GetMapping("/hello")
	public String hi() {
		return "PORT 8113 test";
	}
}
