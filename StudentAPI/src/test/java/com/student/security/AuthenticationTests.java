package com.student.security;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.junit.jupiter.api.function.Executable;
@SpringBootTest
public class AuthenticationTests {

	@Autowired private AuthenticationManager authManger;
	
	@Test
	public void testAuthenticationFailed() {
		assertThrows(BadCredentialsException.class, new Executable() {
			
			@Override
			public void execute() throws Throwable {
			
				authManger.authenticate(new UsernamePasswordAuthenticationToken("sany", "xyz"));
				
			}
		});
		
	}
	
	@Test
	public void testAuthenticationSuccess() {
		String userName = "sany";
		String password = "ynas";
		
		Authentication authentication = authManger.authenticate(new UsernamePasswordAuthenticationToken(userName, password));
		
		assertThat(authentication.isAuthenticated()).isTrue();
		
		CustomUserDetils user =(CustomUserDetils) authentication.getPrincipal();
		
		assertThat(user).isInstanceOf(CustomUserDetils.class);
		assertThat(user.getUsername()).isEqualTo(userName);
		
		
				
	}
}
