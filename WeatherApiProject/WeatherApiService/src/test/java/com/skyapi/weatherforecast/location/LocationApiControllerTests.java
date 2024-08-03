package com.skyapi.weatherforecast.location;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skyapi.weatherforecast.common.Location;

@WebMvcTest(LocationApiController.class)
public class LocationApiControllerTests {

	private static final String END_URI_PATH = "/v1/locations";
	
	@Autowired  MockMvc mockMvc;
	@Autowired  ObjectMapper mapper;
	@MockBean  LocationService service;
	
	
	@Test
	public void testAddLocationShouldReturn400BadRequest() throws Exception {
		
		
		Location location =  new Location();
		
		String bodyContent = mapper.writeValueAsString(location);
		mockMvc.perform(post(END_URI_PATH).contentType("application/json")
				                          .content(bodyContent)).andExpect(status()
				                        		  .isBadRequest()).andDo(print());
		
	}
	
	@Test
	public void testAddLoactionShouldReturn201Created() throws Exception {
		
		Location location = new Location();
		location.setCode("UCH_PK");
		location.setCityName("Uch Sharif");
		location.setRegionName("Bahwalpur");
		location.setCounrtyCode("PK");
		location.setCountryName("Pakistan");
		
		Mockito.when(service.add(location)).thenReturn(location);
		
		String bodyContent = mapper.writeValueAsString(location);
		mockMvc.perform(post(END_URI_PATH).contentType("application/json")
				                          .content(bodyContent))
		                                  .andExpect(status() .isCreated())
		                                  .andExpect(jsonPath("$.code", is("UCH_PK")))
		                                  .andExpect(jsonPath("$.city_name", is("Uch Sharif")))
		                                  .andExpect(header().string("Location", "/v1/locations/UCH_PK"))
		                                  .andDo(print());
		
		
	}
}
