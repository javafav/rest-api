package com.student.security.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import com.student.repository.User;

import io.jsonwebtoken.JwtException;

public class JwtUtilityTests {

	private static JwtUtility jwtUtility;
	
	@BeforeAll
    static	void setUp() {
		 jwtUtility = new JwtUtility();
		jwtUtility.setIssuerName("My Company");
		jwtUtility.setSecertKey("ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrestuvwxyz@#%didf");
		jwtUtility.setAccessTokenExpiration(2);
		
	}
	
	@Test
	public void testGenrateFail() {
		assertThrows(IllegalArgumentException.class, new Executable() {
			
			@Override
			public void execute() throws Throwable {
				User user = null;
				
				jwtUtility.generateAccessToken(user);
				
			}
		} );
		
	}
	@Test
	public void testGenrateSuccess() {
		User user = new User();
		user.setId(3);
		user.setUsername("john");
		user.setRole("read");
		
		String token = jwtUtility.generateAccessToken(user);
		
		assertThat(token).isNotNull();
		
		System.out.println(token);
		
		
		
	}
	@Test
	public void testGenrateValidateFail() {
		assertThrows(JwtValidationException.class, () -> {
			jwtUtility.validateAccessToken("a.b.c.d.e");
		});
	}
	@Test
	public void testGenrateValidateSuccess() {
		User user = new User();
		user.setId(3);
		user.setUsername("john");
		user.setRole("read");
		
		String token = jwtUtility.generateAccessToken(user);
		
		assertDoesNotThrow( () -> {
			jwtUtility.validateAccessToken(token);
		} );
	}
}
