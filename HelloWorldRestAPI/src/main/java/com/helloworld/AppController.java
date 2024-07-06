package com.helloworld;

import java.util.Date;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppController {

	@GetMapping("/api/hello")
	public String showHelloMessage() {
		return "Hello world REST API . The current time is: " +  new Date();
	}
}
