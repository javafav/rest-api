package com.skyapi.weatherforecast.location;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.anyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skyapi.weatherforecast.common.Location;

@WebMvcTest(LocationApiController.class)
public class LocationApiControllerTests {

	private static final String END_URI_PATH = "/v1/locations";
	private static final String RESPONSE_CONTENT_TYPE = "application/hal+json";
	private static final String REQUEST_CONTENT_TYPE = "application/json";

	@Autowired
	MockMvc mockMvc;
	@Autowired
	ObjectMapper mapper;
	@MockBean
	LocationService service;

	@Test
	public void testAddLocationShouldReturn400BadRequest() throws Exception {

		LocationDTO dto = new LocationDTO();

		String bodyContent = mapper.writeValueAsString(dto);
		mockMvc.perform(post(END_URI_PATH).contentType(REQUEST_CONTENT_TYPE).content(bodyContent))
				.andExpect(status().isBadRequest()).andDo(print());

	}

	@Test
	public void testAddLoactionShouldReturn201Created() throws Exception {

		String code = "UCH_PK";

		Location location = new Location();
		location.setCode(code);
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

		when(service.add(location)).thenReturn(location);

		String bodyContent = mapper.writeValueAsString(dto);

		mockMvc.perform(post(END_URI_PATH).contentType(REQUEST_CONTENT_TYPE).content(bodyContent))
				.andExpect(status().isCreated()).andExpect(content().contentType(RESPONSE_CONTENT_TYPE))
				.andExpect(jsonPath("$.code", is("UCH_PK"))).andExpect(jsonPath("$.city_name", is("Uch Sharif")))
				

				.andExpect(header().string("Location", END_URI_PATH + "/" + code))
				.andExpect(jsonPath("$._links.self.href", is("http://localhost" + END_URI_PATH + "/" + code)))
				.andExpect(jsonPath("$._links.realtime_weather.href", is("http://localhost/v1/realtime/" + code)))
				.andExpect(jsonPath("$._links.hourly_forecast.href", is("http://localhost/v1/hourly/" + code)))
				.andExpect(jsonPath("$._links.daily_forecast.href", is("http://localhost/v1/daily/" + code)))
				.andExpect(jsonPath("$._links.full_forecast.href", is("http://localhost/v1/full/" + code)))
				.andDo(print());

	}

	@Test
	@Disabled
	public void testListShouldReturn204NoContent() throws Exception {
		when(service.list()).thenReturn(Collections.emptyList());
		
		mockMvc.perform(get(END_URI_PATH))
		       .andExpect(status().isNoContent())
		       .andDo(print());
		   
	}

	@Test
	public void testListByPageShouldReturn204NoContent() throws Exception {
		when(service.listByPage(anyInt(), anyInt(), anyString(), anyMap()))
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

		when(service.listByPage(pageNum, pagesize, sortField)).thenReturn(Page.empty());

		mockMvc.perform(get(requestURI)).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors[0]", containsString("must be greater than or equal to 1")))
				.andDo(print());

	}

	@Test
	public void testListByPageShouldReturn400BadRequestBecauseOfInvalidLowerLimitOfPageSize() throws Exception {

		int pageNum = 1;
		int pagesize = 4;
		String sortField = "code";

		String requestURI = END_URI_PATH + "?sort=" + sortField + "&page=" + pageNum + "&size=" + pagesize;

		when(service.listByPage(anyInt(), anyInt(), anyString(), anyMap())).thenReturn(Page.empty());

		mockMvc.perform(get(requestURI)).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors[0]", containsString(" must be greater than or equal to 5")))
				.andDo(print());

	}

	@Test
	public void testListByPageShouldReturn400BadRequestBecauseOfInvalidUpperLimitOfPageSize() throws Exception {

		int pageNum = 1;
		int pagesize = 21;
		String sortField = "code";

		String requestURI = END_URI_PATH + "?sort=" + sortField + "&page=" + pageNum + "&size=" + pagesize;

		when(service.listByPage(anyInt(), anyInt(), anyString(), anyMap())).thenReturn(Page.empty());

		mockMvc.perform(get(requestURI)).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors[0]", containsString(" must be less than or equal to 20"))).andDo(print());

	}

	@Test
	public void testListByPageShouldReturn400BadRequestBecauseOfInvalidSortField() throws Exception {

		int pageNum = 1;
		int pagesize = 5;
		String sortField = "code_abc";

		String requestURI = END_URI_PATH + "?sort=" + sortField + "&page=" + pageNum + "&size=" + pagesize;

		when(service.listByPage(pageNum, pagesize, sortField)).thenReturn(Page.empty());

		mockMvc.perform(get(requestURI)).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors[0]", containsString("Invalid sort field"))).andDo(print());

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
		mockMvc.perform(post(END_URI_PATH).contentType(REQUEST_CONTENT_TYPE).content(bodyContent))
				.andExpect(status().isBadRequest())
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
		mockMvc.perform(post(END_URI_PATH).contentType(REQUEST_CONTENT_TYPE).content(bodyContent))
				.andExpect(status().isBadRequest())
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

		when(service.list()).thenReturn(List.of(location1, location2));

		mockMvc.perform(get(END_URI_PATH)).andExpect(status().isOk()).andExpect(jsonPath("$[0].code", is("UCH_PK")))
				.andExpect(jsonPath("$[0].city_name", is("Uch Sharif"))).andExpect(jsonPath("$[1].code", is("MUX_PK")))
				.andExpect(jsonPath("$[1].city_name", is("Multan")))
//              .andExpect(header().string("Location", "/v1/locations/UCH_PK"))
				.andDo(print());

	}

	@Test
	public void testListByPageShouldReturn200OK() throws Exception {
		Location location1 = new Location();
		location1.setCode("NYC_USA");
		location1.setCityName("New York City");
		location1.setRegionName("New York");
		location1.setCountryCode("US");
		location1.setCountryName("United States of America");
		location1.setEnabled(true);

		Location location2 = new Location();
		location2.setCode("LACA_USA");
		location2.setCityName("Los Angeles");
		location2.setRegionName("California");
		location2.setCountryCode("US");
		location2.setCountryName("United States of America");
		location2.setEnabled(true);

		List<Location> listLocations = List.of(location1, location2);

		int pageSize = 5;
		int pageNum = 1;
		String sortField = "code";
		int totalElements = listLocations.size();

		Sort sort = Sort.by(sortField);
		Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);

		Page<Location> page = new PageImpl<>(listLocations, pageable, totalElements);

		when(service.listByPage(anyInt(), anyInt(), anyString(), anyMap() )).thenReturn(page);

		String requestURI = END_URI_PATH + "?page=" + pageNum + "&size=" + pageSize + "&sort=" + sortField;

		mockMvc.perform(get(requestURI)).andExpect(status().isOk())
				.andExpect(content().contentType("application/hal+json"))
				.andExpect(jsonPath("$._embedded.locations[0].code", is("NYC_USA")))
				.andExpect(jsonPath("$._embedded.locations[0].city_name", is("New York City")))
				.andExpect(jsonPath("$._embedded.locations[1].code", is("LACA_USA")))
				.andExpect(jsonPath("$._embedded.locations[1].city_name", is("Los Angeles")))
				.andExpect(jsonPath("$.page.size", is(pageSize))).andExpect(jsonPath("$.page.number", is(pageNum)))
				.andExpect(jsonPath("$.page.total_elements", is(totalElements)))
				.andExpect(jsonPath("$.page.total_pages", is(1))).andDo(print());
	}

	@Test
	public void testDeleteShouldReturn204NoContent() throws Exception {

		String code = "UCH_PK";
		String uriPath = END_URI_PATH + "/" + code;

		doNothing().when(service).delete(code);

		mockMvc.perform(delete(uriPath)).andExpect(status().isNoContent()).andDo(print());
	}

	@Test
	public void testPaginationLinksOnlyOnePage() throws Exception {
		Location location1 = new Location("APE_PK", "Ahmad Pur East", "Punajb", "Pakistan", "PK");
		Location location2 = new Location("BWP_PK", "Bahawalpur", "Punajb", "Pakistan", "PK");

		int pageNum = 1;
		int pageSize = 5;
		String sortField = "code";
		Sort sort = Sort.by(sortField);

		List<Location> listLocations = List.of(location1, location2);
		int totalElements = listLocations.size();

		Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);

		Page<Location> page = new PageImpl<>(listLocations, pageable, totalElements);

		when(service.listByPage(anyInt(), anyInt(), anyString(), anyMap())).thenReturn(page);

		String hostURI = "http://localhost";
		String requestURI = END_URI_PATH + "?page=" + pageNum + "&size=" + pageSize + "&sort=" + sortField;

		mockMvc.perform(get(requestURI)).andExpect(status().isOk())
				.andExpect(content().contentType(RESPONSE_CONTENT_TYPE))
				.andExpect(jsonPath("$._links.self.href", containsString(hostURI + requestURI)))
				.andExpect(jsonPath("$._links.first.href").doesNotExist())
				.andExpect(jsonPath("$._links.next.href").doesNotExist())
				.andExpect(jsonPath("$._links.prev.href").doesNotExist())
				.andExpect(jsonPath("$._links.last.href").doesNotExist())

				.andDo(print());

	}

	@Test
	public void testPaginationLinksFirstPage() throws Exception {
		int totalElemets = 18;
		int pageSize = 5;

		List<Location> listLocations = new ArrayList<>(pageSize);

		for (int i = 1; i <= pageSize; i++) {
			listLocations.add(new Location("Code_" + i, "City_" + i, "Region Name", "Pakistan", "PK"));
		}

		int pageNum = 1;
		int totalPages = totalElemets / pageSize + 1;
		String sortField = "code";

		Sort sort = Sort.by(sortField);
		Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);

		Page<Location> page = new PageImpl<>(listLocations, pageable, totalElemets);

		when(service.listByPage(anyInt(), anyInt(), anyString(), anyMap())).thenReturn(page);

		String hostURI = "http://localhost";

		String requestURI = END_URI_PATH + "?page=" + pageNum + "&size=" + pageSize + "&sort=" + sortField;

		String nextPageURI = END_URI_PATH + "?page=" + (pageNum + 1) + "&size=" + pageSize + "&sort=" + sortField;
		String lastPageURI = END_URI_PATH + "?page=" + totalPages + "&size=" + pageSize + "&sort=" + sortField;

		mockMvc.perform(get(requestURI)).andExpect(status().isOk())
				.andExpect(content().contentType(RESPONSE_CONTENT_TYPE))

				.andExpect(jsonPath("$._links.first.href").doesNotExist())
				.andExpect(jsonPath("$._links.next.href", containsString(hostURI + nextPageURI)))
				.andExpect(jsonPath("$._links.prev.href").doesNotExist())
				.andExpect(jsonPath("$._links.last.href", containsString(hostURI + lastPageURI))).andDo(print());

	}

	@Test
	public void testPaginationLinksMiddlePage() throws Exception {
		int totalElements = 18;
		int pageSize = 5;

		List<Location> listLocations = new ArrayList<>();

		for (int i = 1; i <= pageSize; i++) {
			listLocations.add(new Location("Code_" + i, "City_" + i, "Region Name", "Pakistan", "PK"));
		}

		String sortField = "code";
		Sort sort = Sort.by(sortField);

		int totalPages = (totalElements / pageSize) + 1;
		int pageNum = 3;
		Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);

		Page<Location> page = new PageImpl<>(listLocations, pageable, totalElements);

		when(service.listByPage(anyInt(), anyInt(), anyString(), anyMap())).thenReturn(page);

		String hostURI = "http://localhost";

		String requestURI = END_URI_PATH + "?page=" + pageNum + "&size=" + pageSize + "&sort=" + sortField;

		String firstPageURI = END_URI_PATH + "?page=1&size=" + pageSize + "&sort=" + sortField;
		String nextPageURI = END_URI_PATH + "?page=" + (pageNum + 1) + "&size=" + pageSize + "&sort=" + sortField;
		String prevPageURI = END_URI_PATH + "?page=" + (pageNum - 1) + "&size=" + pageSize + "&sort=" + sortField;

		String lastPageURI = END_URI_PATH + "?page=" + totalPages + "&size=" + pageSize + "&sort=" + sortField;

		mockMvc.perform(get(requestURI)).andExpect(status().isOk())
				.andExpect(content().contentType(RESPONSE_CONTENT_TYPE))
				.andExpect(jsonPath("$._links.self.href", containsString(hostURI + requestURI)))
				.andExpect(jsonPath("$._links.first.href", containsString(hostURI + firstPageURI)))
				.andExpect(jsonPath("$._links.next.href", containsString(hostURI + nextPageURI)))
				.andExpect(jsonPath("$._links.prev.href", containsString(hostURI + prevPageURI)))
				.andExpect(jsonPath("$._links.last.href", containsString(hostURI + lastPageURI))).andDo(print());

	}

	@Test
	public void testPaginationLinksLastPage() throws Exception {
		int totalElements = 18;
		int pageSize = 5;

		List<Location> listLocations = new ArrayList<>();

		for (int i = 1; i <= pageSize; i++) {
			listLocations.add(new Location("Code_" + i, "City_" + i, "Region Name", "Pakistan", "PK"));
		}

		String sortField = "code";
		Sort sort = Sort.by(sortField);

		int totalPages = (totalElements / pageSize) + 1;
		int pageNum = totalPages;
		Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);

		Page<Location> page = new PageImpl<>(listLocations, pageable, totalPages);

		when(service.listByPage(anyInt(), anyInt(), anyString(), anyMap())).thenReturn(page);

		String hostURI = "http://localhost";

		String requestURI = END_URI_PATH + "?page=" + totalPages + "&size=" + pageSize + "&sort=" + sortField;

		String firstPageURI = END_URI_PATH + "?page=1&size=" + pageSize + "&sort=" + sortField;
		String prevPageURI = END_URI_PATH + "?page=" + (pageNum - 1) + "&size=" + pageSize + "&sort=" + sortField;

		mockMvc.perform(get(requestURI)).andExpect(status().isOk())
				.andExpect(content().contentType(RESPONSE_CONTENT_TYPE))
				.andExpect(jsonPath("$._links.self.href", containsString(hostURI + requestURI)))
				.andExpect(jsonPath("$._links.first.href", containsString(hostURI + firstPageURI)))
				.andExpect(jsonPath("$._links.next.href").doesNotExist())
				.andExpect(jsonPath("$._links.prev.href", containsString(hostURI + prevPageURI)))

				.andDo(print());

	}

	@Test
	public void testShouldReturn405MethodNotAllowed() throws Exception {
		String uriPath = END_URI_PATH + "/ABCD";

		mockMvc.perform(post(uriPath)).andExpect(status().isMethodNotAllowed()).andDo(print());
	}

	@Test
	public void testShouldReturn400NoContenetFound() throws Exception {
		String uriPath = END_URI_PATH + "/ABCD";

		mockMvc.perform(delete(uriPath)).andExpect(status().isNoContent()).andDo(print());
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

		when(service.get(code)).thenReturn(location);

		mockMvc.perform(get(uriPath)).andExpect(status().isOk())
		        .andExpect(content().contentType(RESPONSE_CONTENT_TYPE))
		        .andExpect(jsonPath("$.city_name", is("Uch Sharif")))
				.andExpect(jsonPath("$.code", is(code)))
				.andExpect(jsonPath("$.city_name", is("Uch Sharif")))
				.andExpect(jsonPath("$._links.self.href", is("http://localhost" + END_URI_PATH + "/" + code)))
				.andExpect(jsonPath("$._links.realtime_weather.href", is("http://localhost/v1/realtime/" + code)))
				.andExpect(jsonPath("$._links.hourly_forecast.href", is("http://localhost/v1/hourly/" + code)))
				.andExpect(jsonPath("$._links.daily_forecast.href", is("http://localhost/v1/daily/" + code)))
				.andExpect(jsonPath("$._links.full_forecast.href", is("http://localhost/v1/full/" + code)))
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

		when(service.update(location))
				.thenThrow(new LocationNotFoundException("No location found with given code" + location.getCode()));
		// LocationNotFoundException ex = new
		// LocationNotFoundException(location.getCityName());

		// Mockito.when(service.update(Mockito.any())).thenThrow(ex);

		String bodyContent = mapper.writeValueAsString(dto);

		mockMvc.perform(put(END_URI_PATH).contentType(REQUEST_CONTENT_TYPE).content(bodyContent))
				.andExpect(status().isNotFound()).andDo(print());

	}

	@Test
	public void testUpdateShouldReturn400BadRequest() throws Exception {

		Location location = new Location();

		location.setCityName("Uch Sharif");
		location.setRegionName("Bahwalpur");
		location.setCountryCode("PK");
		location.setCountryName("Pakistan");

		String bodyContent = mapper.writeValueAsString(location);

		// Mockito.when(service.update(location)).thenThrow(new
		// LocationNotFoundException("No location found with given code" +
		// location.getCode()));

		mockMvc.perform(put(END_URI_PATH).contentType(REQUEST_CONTENT_TYPE).content(bodyContent))
				.andExpect(status().isBadRequest()).andDo(print());

	}

	@Test
	public void testUpdateShouldReturn200OK() throws Exception {
		String code = "UCH_PK";
		Location location = new Location();
		location.setCode(code);
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

		when(service.update(location)).thenReturn(location);

		mockMvc.perform(put(END_URI_PATH).contentType(REQUEST_CONTENT_TYPE).content(bodyContent))
				
				
				.andExpect(status().isOk())
				.andExpect(content().contentType(RESPONSE_CONTENT_TYPE))
				.andExpect(jsonPath("$.code", is("UCH_PK")))
				.andExpect(jsonPath("$.city_name", is("Uch Sharif")))
				.andExpect(jsonPath("$._links.self.href", is("http://localhost" + END_URI_PATH + "/" + code)))
				.andExpect(jsonPath("$._links.realtime_weather.href", is("http://localhost/v1/realtime/" + code)))
				.andExpect(jsonPath("$._links.hourly_forecast.href", is("http://localhost/v1/hourly/" + code)))
				.andExpect(jsonPath("$._links.daily_forecast.href", is("http://localhost/v1/daily/" + code)))
				.andExpect(jsonPath("$._links.full_forecast.href", is("http://localhost/v1/full/" + code)))			
				.andDo(print());	


	}

	@Test
	public void testDeleteShouldReturn404NotFound() throws Exception {

		String code = "UCH";
		String uriPath = END_URI_PATH + "/" + code;

		doThrow(new LocationNotFoundException("No location found with given code " + code)).when(service)
				.delete(code);

		mockMvc.perform(delete(uriPath)).andExpect(status().isNotFound()).andDo(print());
	}

}
