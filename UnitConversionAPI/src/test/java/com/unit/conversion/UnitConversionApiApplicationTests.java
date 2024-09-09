package com.unit.conversion;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unit.conversion.config.CustomUserDetailsService;
import com.unit.conversion.config.SecurityConfig;
import com.unit.conversion.repo.UserRepository;
import com.unit.conversion.user.User;

@WebMvcTest(UnitConversionAPI.class)
@Import(SecurityConfig.class)
public class UnitConversionApiApplicationTests {

	private static final String REQUEST_URI = "/api/unit-conversion";
	
	@Autowired private ObjectMapper objectMapper;
	@Autowired private MockMvc mockMvc;
	@MockBean private UserRepository userRepo;
	
	@Test
	public void testShouldReturn401BecauseNoCredentials() throws Exception {
		ConverstionDetails details = new ConverstionDetails();
		
		details.setFromUnit("km");
		details.setToUnit("mile");
		details.setFromValue(100);
		
		String bodyContent = objectMapper.writeValueAsString(details);
		
		mockMvc.perform(post(REQUEST_URI).contentType("application/json").content(bodyContent))
		.andExpect(status().isUnauthorized())
		.andDo(print());
		
	}
	
	@Test
	public void testShouldReturn200BecauseValidCredentials() throws Exception {
		String userName = "sany";
		String rawPassword = "khan";
		String encodePsString = "$2a$10$x.WHXs1mJwk/msHghaxNVepItSqJec6dERw8URweWetivzP6hsGtS";
		
		ConverstionDetails details = new ConverstionDetails();
		details.setFromUnit("km");
		details.setToUnit("mile");
		details.setFromValue(100);
		
		Mockito.when(userRepo.findByUserName(userName)).thenReturn( new User(userName, encodePsString));
		
		String bodyContent = objectMapper.writeValueAsString(details);
		
		mockMvc.perform(post(REQUEST_URI).contentType("application/json").content(bodyContent).with(httpBasic(userName, rawPassword)))
		.andExpect(status().isOk())
		.andDo(print());
		
	}
	
	@Test
	public void testShouldReturn401BecauseInValidCredentials() throws Exception {
		String userName = "sany";
		String rawPassword = "khan123";
		String encodePsString = "$2a$10$x.WHXs1mJwk/msHghaxNVepItSqJec6dERw8URweWetivzP6hsGtS";
		
		ConverstionDetails details = new ConverstionDetails();
		details.setFromUnit("km");
		details.setToUnit("mile");
		details.setFromValue(100);
		
		Mockito.when(userRepo.findByUserName(userName)).thenReturn( new User(userName, encodePsString));
		
		String bodyContent = objectMapper.writeValueAsString(details);
		
		mockMvc.perform(post(REQUEST_URI).contentType("application/json").content(bodyContent).with(httpBasic(userName, rawPassword)))
		.andExpect(status().isUnauthorized())
		.andDo(print());
		
	}
}
