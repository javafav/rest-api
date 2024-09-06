package com.helloworld;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(ApiController.class)
@Import(SecurityConfig.class)
public class ApiControllerTests {
	private static final String ENDPOINT = "/api/hello";
	
	@Autowired MockMvc mvc;
	
	@Test
	public void shouldReturn401BecauseNoCredentials() throws Exception {
		mvc.perform(get(ENDPOINT)).andExpect(status().isUnauthorized()).andDo(print());
	}
	
	@Test
	public void shouldReturn200BecauseValidCredentials() throws Exception {
		
		mvc.perform(get(ENDPOINT).with(httpBasic("admin", "nimda")))
			.andExpect(status().isOk()).andDo(print());
	}
	
	@Test
	public void shouldReturn401BecauseInvalidCredentials() throws Exception {
		
		mvc.perform(get(ENDPOINT).with(httpBasic("johndoe", "nimda")))
			.andExpect(status().isUnauthorized()).andDo(print());
	}	
}
