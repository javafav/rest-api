package com.skyapi.weatherforecast.location;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collection;
import java.util.Collections;import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
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
	
	@Test
	public void testListShouldReturn204NoContent() throws Exception {
		Mockito.when(service.list()).thenReturn(Collections.emptyList());
		
		mockMvc.perform(get(END_URI_PATH))
		       .andExpect(status().isNoContent())
		       .andDo(print());
		   
	}
	
	
	@Test
	public void testShouldReturn200OK() throws Exception {
		Location location1 = new Location();
		location1.setCode("UCH_PK");
		location1.setCityName("Uch Sharif");
		location1.setRegionName("Bahwalpur");
		location1.setCounrtyCode("PK");
		location1.setCountryName("Pakistan");
		
		
		Location location2 = new Location();
		location2.setCode("MUX_PK");
		location2.setCityName("Multan");
		location2.setRegionName("multan");
		location2.setCounrtyCode("PK");
		location2.setCountryName("Pakistan");
		
		Mockito.when(service.list()).thenReturn(List.of(location1, location2));
		
		mockMvc.perform(get(END_URI_PATH))
                .andExpect(status() .isOk())
                .andExpect(jsonPath("$[0].code", is("UCH_PK")))
                .andExpect(jsonPath("$[0].city_name", is("Uch Sharif")))
                .andExpect(jsonPath("$[1].code", is("MUX_PK")))
                .andExpect(jsonPath("$[1].city_name", is("Multan")))
//                .andExpect(header().string("Location", "/v1/locations/UCH_PK"))
                .andDo(print());
		
		
	}
	
	
	@Test
	public void testShouldReturn405MethodNotAllowed() throws Exception {
		String uriPath = END_URI_PATH + "/ABCD";
		
		
		mockMvc.perform(post(uriPath))
		       .andExpect(status().isMethodNotAllowed())
		       .andDo(print());
	}
	
	@Test
	public void testShouldReturn400NoContenetFound() throws Exception {
		String uriPath = END_URI_PATH + "/ABCD";
		
		
		mockMvc.perform(get(uriPath))
		       .andExpect(status().isNoContent())
		       .andDo(print());
	}
	
	@Test
	public void testLocationShouldReturn200OK() throws Exception {
		String code = "UCH_PK";
		String uriPath = END_URI_PATH + "/" + code;
		
		Location location = new Location();
		location.setCode("UCH_PK");
		location.setCityName("Uch Sharif");
		location.setRegionName("Bahwalpur");
		location.setCounrtyCode("PK");
		location.setCountryName("Pakistan");
		
		Mockito.when(service.get(code)).thenReturn(location);
		
		
		
		mockMvc.perform(get(uriPath ) )
               .andExpect(status() .isOk())
               .andExpect(jsonPath("$.code", is("UCH_PK")))
               .andExpect(jsonPath("$.city_name", is("Uch Sharif")))
        
//        .andExpect(header().string("Location", "/v1/locations/UCH_PK"))
               .andDo(print());
		

	}
    
	@Test
	public void testUpdateShouldReturn404NotFound() throws Exception {
		
		Location location = new Location();
		location.setCode("ABCDE");
		location.setCityName("Uch Sharif");
		location.setRegionName("Bahwalpur");
		location.setCounrtyCode("PK");
		location.setCountryName("Pakistan");
		
		String bodyContent = mapper.writeValueAsString(location);
		
		Mockito.when(service.update(location)).thenThrow(new LocationNotFoundException("No location found with given code" + location.getCode()));
		
		mockMvc.perform(put(END_URI_PATH).contentType("application/json")
				                         .content(bodyContent))
		                                 .andExpect(status().isNotFound())
		                                 .andDo(print());
		
	}

	@Test
	public void testUpdateShouldReturn400BadRequest() throws Exception {
		
		Location location = new Location();
		
		location.setCityName("Uch Sharif");
		location.setRegionName("Bahwalpur");
		location.setCounrtyCode("PK");
		location.setCountryName("Pakistan");
		
		String bodyContent = mapper.writeValueAsString(location);
		
		//Mockito.when(service.update(location)).thenThrow(new LocationNotFoundException("No location found with given code" + location.getCode()));
		
		mockMvc.perform(put(END_URI_PATH).contentType("application/json")
				                         .content(bodyContent))
		                                 .andExpect(status().isBadRequest())
		                                 .andDo(print());
		
	}

	@Test
	public void testUpdateShouldReturn200OK() throws Exception {
		Location location = new Location();
		location.setCode("UCH_PK");
		location.setCityName("Uch Sharif");
		location.setRegionName("Bahwalpur");
		location.setCounrtyCode("PK");
		location.setCountryName("Pakistan");
		
		String bodyContent = mapper.writeValueAsString(location);
		
		Mockito.when(service.update(location)).thenReturn(location);
		
		mockMvc.perform(put(END_URI_PATH).contentType("application/json")
						                 .content(bodyContent))
						                 .andExpect(status() .isOk())
						                 .andExpect(jsonPath("$.code", is("UCH_PK")))
						                 .andExpect(jsonPath("$.city_name", is("Uch Sharif")))
              
//                .andExpect(header().string("Location", "/v1/locations/UCH_PK"))
                .andDo(print());
		
		
	}
	
	
	@Test
	public void testDeleteShouldReturn404NotFound() throws Exception {
		
		String code = "UCH";
		String uriPath = END_URI_PATH + "/" + code;
		
		 Mockito.doThrow(new LocationNotFoundException("No location found with given code " + code))
         .when(service).delete(code);
		
			mockMvc.perform(delete(uriPath))
		       .andExpect(status().isNotFound())
		       .andDo(print());
	}
	
	
	@Test
	public void testDeleteShouldReturn204NoContent() throws Exception {
		
		String code = "UCH_PK";
		String uriPath = END_URI_PATH + "/" + code;
		
		Mockito.doNothing().when(service).delete(code);
		
			mockMvc.perform(delete(uriPath))
		       .andExpect(status().isNoContent())
		       .andDo(print());
	}
}
