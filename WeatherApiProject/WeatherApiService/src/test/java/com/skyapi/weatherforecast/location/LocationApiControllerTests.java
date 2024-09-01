package com.skyapi.weatherforecast.location;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

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
		
		
		LocationDTO dto =  new LocationDTO();
		
		String bodyContent = mapper.writeValueAsString(dto);
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
		location.setCountryCode("PK");
		location.setCountryName("Pakistan");
		
		LocationDTO dto = new LocationDTO();
		dto.setCode(location.getCode());
		dto.setCityName(location.getCityName());
		dto.setRegionName(location.getRegionName());
		dto.setCountryCode(location.getCountryCode());
		dto.setCountryName(location.getCountryName());
		dto.setEnabled(location.isEnabled());
		
		Mockito.when(service.add(location)).thenReturn(location);
		
		String bodyContent = mapper.writeValueAsString(dto);
		
		mockMvc.perform(post(END_URI_PATH).contentType("application/json")
				                          .content(bodyContent))
		                                  .andExpect(status() .isCreated())
		                                  .andExpect(jsonPath("$.code", is("UCH_PK")))
		                                  .andExpect(jsonPath("$.city_name", is("Uch Sharif")))
		                                  .andExpect(header().string("Location", "/v1/locations/UCH_PK"))
		                                  .andDo(print());
		
		
	}
	
	@Test
	@Disabled
	public void testListShouldReturn204NoContent() throws Exception {
		Mockito.when(service.list()).thenReturn(Collections.emptyList());
		
		mockMvc.perform(get(END_URI_PATH))
		       .andExpect(status().isNoContent())
		       .andDo(print());
		   
	}
	
	
	
	@Test
	public void testListByPageShouldReturn204NoContent() throws Exception {
		Mockito.when(service.listByPage(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString()))
	       .thenReturn(Page.empty());

		
		mockMvc.perform(get(END_URI_PATH))
		       .andExpect(status().isNoContent())
		       .andDo(print());
		   
	}
	
	
	@Test
	public void testListByPageShouldReturn400BadRequestBecauseOfInvlaidPageNum() throws Exception {
	
		int pageNum = 0;
		int pagesize = 5;
		String sortField = "code";
		
		
		String requestURI = END_URI_PATH + "?sort=" + sortField + "&page=" + pageNum + "&size=" + pagesize;
		
		Mockito.when(service.listByPage(pageNum, pagesize, sortField))
	       .thenReturn(Page.empty());

		
		mockMvc.perform(get(requestURI))
		       .andExpect(status().isBadRequest())
		       .andExpect(jsonPath("$.errors[0]", containsString("must be greater than or equal to 1")))
		       .andDo(print());
		   
	}
	
	@Test
	public void testListByPageShouldReturn400BadRequestBecauseOfInvalidLowerLimitOfPageSize() throws Exception {
	
		int pageNum = 1;
		int pagesize = 3;
		String sortField = "code";
		
		
		String requestURI = END_URI_PATH + "?sort=" + sortField + "&page=" + pageNum + "&size=" + pagesize;
		
		Mockito.when(service.listByPage(pageNum, pagesize, sortField))
	       .thenReturn(Page.empty());

		
		mockMvc.perform(get(requestURI))
		       .andExpect(status().isBadRequest())
		       .andExpect(jsonPath("$.errors[0]", containsString(" must be greater than or equal to 5")))
		       .andDo(print());
		   
	}

	
	@Test
	public void testListByPageShouldReturn400BadRequestBecauseOfInvalidUpperLimitOfPageSize() throws Exception {
	
		int pageNum = 1;
		int pagesize = 21;
		String sortField = "code";
		
		
		String requestURI = END_URI_PATH + "?sort=" + sortField + "&page=" + pageNum + "&size=" + pagesize;
		
		Mockito.when(service.listByPage(pageNum, pagesize, sortField))
	       .thenReturn(Page.empty());

		
		mockMvc.perform(get(requestURI))
		       .andExpect(status().isBadRequest())
		       .andExpect(jsonPath("$.errors[0]", containsString(" must be less than or equal to 20")))
		       .andDo(print());
		   
	}
	
	@Test
	public void testListByPageShouldReturn400BadRequestBecauseOfInvalidSortField() throws Exception {
	
		int pageNum = 1;
		int pagesize = 5;
		String sortField = "code_abc";
		
		
		String requestURI = END_URI_PATH + "?sort=" + sortField + "&page=" + pageNum + "&size=" + pagesize;
		
		Mockito.when(service.listByPage(pageNum, pagesize, sortField))
	       .thenReturn(Page.empty());

		
		mockMvc.perform(get(requestURI))
		       .andExpect(status().isBadRequest())
		       .andExpect(jsonPath("$.errors[0]", containsString("Invalid sort field")))
		       .andDo(print());
		   
	}
	
	
	@Test
	public void testValidateRequestBodyLocationCode() throws Exception {
		Location location = new Location();
		
		location.setCityName("Uch Sharif");
		location.setRegionName("Bahwalpur");
		location.setCountryCode("PK");
		location.setCountryName("Pakistan");
		
		LocationDTO dto = new LocationDTO();
		dto.setCode(location.getCode());
		dto.setCityName(location.getCityName());
		dto.setRegionName(location.getRegionName());
		dto.setCountryCode(location.getCountryCode());
		dto.setCountryName(location.getCountryName());
		dto.setEnabled(location.isEnabled());
		
		
		
		String bodyContent = mapper.writeValueAsString(dto);
		mockMvc.perform(post(END_URI_PATH).contentType("application/json")
				                          .content(bodyContent))
		                                  .andExpect(status() .isBadRequest())
		                                  .andExpect(jsonPath("$.errors[0]", is("Location code cannot be null")))
		               
		                                  .andDo(print());
		
	}
	
	@Test
	public void testValidateRequestBodyLocationCodeLength() throws Exception {
		Location location = new Location();
		location.setCode("");
		location.setCityName("Uch Sharif");
		location.setRegionName("Bahwalpur");
		location.setCountryCode("PK");
		location.setCountryName("Pakistan");
		
		LocationDTO dto = new LocationDTO();
		dto.setCode(location.getCode());
		dto.setCityName(location.getCityName());
		dto.setRegionName(location.getRegionName());
		dto.setCountryCode(location.getCountryCode());
		dto.setCountryName(location.getCountryName());
		dto.setEnabled(location.isEnabled());
		
		
		String bodyContent = mapper.writeValueAsString(dto);
		mockMvc.perform(post(END_URI_PATH).contentType("application/json")
				                          .content(bodyContent))
		                                  .andExpect(status() .isBadRequest())
		                                  .andExpect(jsonPath("$.errors[0]", is("Location code must have 3-12 charcter")))
		               
		                                  .andDo(print());
		
	}
	
	@Test
	public void testValidateRequestBodyAllFiedlInvalid() throws Exception {
		LocationDTO location = new LocationDTO();
		location.setRegionName("");

		String bodyContent = mapper.writeValueAsString(location);

		MvcResult mvcResult = mockMvc.perform(post(END_URI_PATH).contentType("application/json").content(bodyContent))
				.andExpect(status().isBadRequest())

				.andDo(print()).andReturn();
		
		String responseBody = mvcResult.getResponse().getContentAsString();
		
		assertThat(responseBody.contains("Location code must have 3-12 charcter"));
		assertThat(responseBody.contains("City name must have 3-128 charcter"));
		assertThat(responseBody.contains("Region name must have 3-64 charcter"));
		assertThat(responseBody.contains("Country name must have 3-64 charcter"));
		assertThat(responseBody.contains("Country code cannot be null"));

	}
	
	
	@Test
	@Disabled
	public void testListShouldReturn200OK() throws Exception {
		Location location1 = new Location();
		location1.setCode("UCH_PK");
		location1.setCityName("Uch Sharif");
		location1.setRegionName("Bahwalpur");
		location1.setCountryCode("PK");
		location1.setCountryName("Pakistan");
		
		
		Location location2 = new Location();
		location2.setCode("MUX_PK");
		location2.setCityName("Multan");
		location2.setRegionName("multan");
		location2.setCountryCode("PK");
		location2.setCountryName("Pakistan");
		
		Mockito.when(service.list()).thenReturn(List.of(location1, location2));
		
		mockMvc.perform(get(END_URI_PATH))
                .andExpect(status() .isOk())
                .andExpect(jsonPath("$[0].code", is("UCH_PK")))
                .andExpect(jsonPath("$[0].city_name", is("Uch Sharif")))
                .andExpect(jsonPath("$[1].code", is("MUX_PK")))
                .andExpect(jsonPath("$[1].city_name", is("Multan")))
//              .andExpect(header().string("Location", "/v1/locations/UCH_PK"))
                .andDo(print());
		
		
	}
	
	
	@Test
	@Disabled
	public void testListByPageShouldReturn200OK() throws Exception {
		Location location1 = new Location();
		location1.setCode("UCH_PK");
		location1.setCityName("Uch Sharif");
		location1.setRegionName("Bahwalpur");
		location1.setCountryCode("PK");
		location1.setCountryName("Pakistan");
		
		
		Location location2 = new Location();
		location2.setCode("MUX_PK");
		location2.setCityName("Multan");
		location2.setRegionName("multan");
		location2.setCountryCode("PK");
		location2.setCountryName("Pakistan");
		
		Page<Location> page = new PageImpl<>(List.of(location1, location2));
		
		when(service.listByPage(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyString()))
				    .thenReturn(page);
		
		mockMvc.perform(get(END_URI_PATH))
                .andExpect(status() .isOk())
                .andExpect(jsonPath("$[0].code", is("UCH_PK")))
                .andExpect(jsonPath("$[0].city_name", is("Uch Sharif")))
                .andExpect(jsonPath("$[1].code", is("MUX_PK")))
                .andExpect(jsonPath("$[1].city_name", is("Multan")))
//              .andExpect(header().string("Location", "/v1/locations/UCH_PK"))
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
		
		
		mockMvc.perform(delete(uriPath))
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
		location.setCountryCode("PK");
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
		location.setCountryCode("PK");
		location.setCountryName("Pakistan");
		
		
		LocationDTO dto = new LocationDTO();
		dto.setCode(location.getCode());
		dto.setCityName(location.getCityName());
		dto.setRegionName(location.getRegionName());
		dto.setCountryCode(location.getCountryCode());
		dto.setCountryName(location.getCountryName());
		dto.setEnabled(location.isEnabled());
	
		
		Mockito.when(service.update(location)).thenThrow(new LocationNotFoundException("No location found with given code" + location.getCode()));
	//LocationNotFoundException ex = new LocationNotFoundException(location.getCityName());
		
		//Mockito.when(service.update(Mockito.any())).thenThrow(ex);
		
		String bodyContent = mapper.writeValueAsString(dto);
		
	
		
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
		location.setCountryCode("PK");
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
		location.setCountryCode("PK");
		location.setCountryName("Pakistan");
		
		LocationDTO dto = new LocationDTO();
		dto.setCode(location.getCode());
		dto.setCityName(location.getCityName());
		dto.setRegionName(location.getRegionName());
		dto.setCountryCode(location.getCountryCode());
		dto.setCountryName(location.getCountryName());
		dto.setEnabled(location.isEnabled());
		
		String bodyContent = mapper.writeValueAsString(dto);
		
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
