package com.helloworld;

import java.security.Principal;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ApiController {

	@GetMapping("/api/hello")
	public Response showHelloMessage(Principal principal) {
	//	return "Hello world REST API . The current time is: " +  new Date();
		return new Response("Hello World " + principal.getName());  
	}
}
