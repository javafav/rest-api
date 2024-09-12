package com.student.security;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.student.repository.Student;
import com.student.security.jwt.auth.AuthRequest;
import com.student.security.jwt.auth.AuthResponse;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityTests {

	private static final String GET_ACCESS_TOKEN_END_POINT = "/api/oauth/token";
	private static final String GET_LIST_STUDENT_END_POINT = "/api/students?pageNum=1&pageSize=10";
	
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
	
	@Test
	public void testListStudentFail() throws Exception {
		
		mockMvc.perform(get(GET_LIST_STUDENT_END_POINT)
				.content("application/json").header("Authorization", "Bearer somethinginvalid"))
		        .andExpect(status().isUnauthorized())
		        .andDo(print())
		        .andExpect(jsonPath("$.errors[0]").isNotEmpty());
		          
		
	}
	
	
	@Test
	public void testListStudentSuccess() throws Exception {
		AuthRequest request = new AuthRequest();
		request.setUsername("sany");
		request.setPassword("ynas");
		
		String requestBody = objectMapper.writeValueAsString(request);
		
		MvcResult mvcResult = mockMvc.perform(post(GET_ACCESS_TOKEN_END_POINT)
							.contentType("application/json").content(requestBody))
						.andDo(print())
						.andExpect(status().isOk())
						.andExpect(jsonPath("$.accessToken").isNotEmpty())
						.andExpect(jsonPath("$.refreshToken").isNotEmpty())
						.andReturn();
		
		String responseBody = mvcResult.getResponse().getContentAsString();
		
		AuthResponse response = objectMapper.readValue(responseBody, AuthResponse.class);
		
		String bearerToken = "Bearer " + response.getAccessToken();
		
		mockMvc.perform(get(GET_LIST_STUDENT_END_POINT)
				.header("Authorization", bearerToken))
			.andDo(print())
	
			.andExpect(jsonPath("$[0].id").isNumber())
			.andExpect(jsonPath("$[0].name").isString())
				.andExpect(status().isOk());		
		
	}
	
	@Test
	public void testAddStudent1() throws Exception {
		String apiEndpoint = "/api/students";
		
		Student student = new Student();
		student.setName("Khan");
		
		
		String requestBody = objectMapper.writeValueAsString(student);
		
		mockMvc.perform(post(apiEndpoint)
				.contentType("application/json").content(requestBody).with(jwt()
				.authorities(new SimpleGrantedAuthority("write"))))
		        .andExpect(status().isCreated())
		        .andDo(print())
		        .andExpect(jsonPath("$.id").isNumber())
				.andExpect(jsonPath("$.name").isString());

		
		
		
	}
	
	@Test
	public void testAddStudent2() throws Exception {
		String apiEndpoint = "/api/students";
		
		Student student = new Student();
		student.setName("Nam Ha Minh");
		
		String requestBody = objectMapper.writeValueAsString(student);
		
		mockMvc.perform(post(apiEndpoint).contentType("application/json").content(requestBody)
				.with(jwt().jwt(jwt -> jwt.claim("scope", "write"))))
			.andDo(print())
			.andExpect(status().isCreated())
			.andExpect(jsonPath("$.id").isNumber())
			.andExpect(jsonPath("$.name").isString());			
			;
		
	}	
	
	@Test
	public void testUpdateStudent1() throws Exception {
		String apiEndpoint = "/api/students";
		
		Student student = new Student();
		student.setId(8);
		student.setName("John Max");
		
		var jwt = org.springframework.security.oauth2.jwt.Jwt.withTokenValue("xxxx")
			.header("alg", "none")
			.issuer("My Company")
			.claim("scope", "write")
			.subject("1,namhm")
			.build();
		
		String requestBody = objectMapper.writeValueAsString(student);
		
		mockMvc.perform(put(apiEndpoint).contentType("application/json").content(requestBody)
				.with(jwt().jwt(jwt)))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").isNumber())
			.andExpect(jsonPath("$.name").isString());			
			;
		
	}	
	
	@Test
	public void testUpdateStudent2() throws Exception {
		String apiEndpoint = "/api/students";
		
		Student student = new Student();
		student.setId(4);
		student.setName("Maryam Ali");
		
		var jwt = org.springframework.security.oauth2.jwt.Jwt.withTokenValue("xxxx")
			.header("alg", "none")
			.issuer("My Company")
			.subject("1,namhm")
			.build();
		
		String requestBody = objectMapper.writeValueAsString(student);
		
		var authorities = AuthorityUtils.createAuthorityList("SCOPE_write");
		
		var token = new JwtAuthenticationToken(jwt, authorities);
		
		mockMvc.perform(put(apiEndpoint).contentType("application/json").content(requestBody)
				.with(authentication(token)))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").isNumber())
			.andExpect(jsonPath("$.name").isString());			
			;
		
	}
	
	@Test
	public void testDeleteStudnet() throws Exception {
		Integer studentId = 7;
		String apiEndpoint = "/api/students/" + studentId;
		var authorities = AuthorityUtils.createAuthorityList("write");
		
		mockMvc.perform(delete(apiEndpoint).content("application/json").with(jwt() .authorities(authorities)))
		    .andDo(print())
		    .andExpect(status().isNoContent());
	}
	
}
