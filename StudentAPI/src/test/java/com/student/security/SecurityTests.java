package com.student.security;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.student.security.jwt.auth.AuthRequest;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityTests {

	private static final String GET_ACCESS_TOKEN_END_POINT = "/api/oauth/token";
	
	@Autowired MockMvc mockMvc;
	@Autowired ObjectMapper objectMapper;
	
	@Test
	public void testGetBaseURIShouldReturn401() throws Exception {
		mockMvc.perform(get("/"))
		.andExpect(status().isUnauthorized())
		.andDo(print());
	}
	
	@Test
	public void testGetAccessTokenBadRequest() throws Exception {
		AuthRequest request = new AuthRequest();
		request.setUsername("saf");
		request.setPassword("12345");
		
		String requestBody = objectMapper.writeValueAsString(request);
		
		mockMvc.perform(post(GET_ACCESS_TOKEN_END_POINT)
				.contentType("application/json").content(requestBody))
				.andDo(print())    
				.andExpect(status().isBadRequest());
		        
	}
	
	
	@Test
	public void testGetAccessFail() throws Exception {
		AuthRequest request = new AuthRequest();
		request.setUsername("javafav");
		request.setPassword("abcdef");
		
		String requestBody = objectMapper.writeValueAsString(request);
		
		mockMvc.perform(post(GET_ACCESS_TOKEN_END_POINT)
				.contentType("application/json").content(requestBody))
				.andDo(print())    
				.andExpect(status().isUnauthorized());
		        
	}
	
	@Test
	public void testGetAccessSuccess() throws Exception {
		AuthRequest request = new AuthRequest();
		request.setUsername("sany");
		request.setPassword("ynas");
		
		String requestBody = objectMapper.writeValueAsString(request);
		
		mockMvc.perform(post(GET_ACCESS_TOKEN_END_POINT)
				.contentType("application/json").content(requestBody))
				.andDo(print())    
				
				.andExpect(jsonPath("$.accessToken").isNotEmpty())
				.andExpect(jsonPath("$.refreshToken").isNotEmpty())
				.andExpect(status().isOk());
		
		        
	}
	
}
