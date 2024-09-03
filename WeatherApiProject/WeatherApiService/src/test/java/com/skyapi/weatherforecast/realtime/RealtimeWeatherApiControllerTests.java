package com.skyapi.weatherforecast.realtime;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skyapi.weatherforecast.GeoLocationException;
import com.skyapi.weatherforecast.GeolocationService;
import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.common.RealtimeWeather;
import com.skyapi.weatherforecast.location.LocationNotFoundException;

@WebMvcTest(RealtimeWeatherApiController.class)
public class RealtimeWeatherApiControllerTests {

	private static final String END_URI_PATH = "/v1/realtime";
	private static final String RESPONSE_CONTENT_TYPE = "application/hal+json";
	private static final String REQUEST_CONTENT_TYPE = "application/json";
	
	@Autowired
	MockMvc mockMvc;
	@Autowired
	ObjectMapper mapper;

	@MockBean
	GeolocationService geolocationService;
	@MockBean
	RealtimeWeatherService realtimeWeatherService;

	@Test
	public void testGetShouldReturn400BadReqest() throws Exception {

		Mockito.when(geolocationService.getLocation(anyString())).thenThrow(GeoLocationException.class);
		mockMvc.perform(get(END_URI_PATH)).andExpect(status().isBadRequest()).andDo(print());
	}

	@Test
	public void testGetShouldReturn404NotFound() throws Exception {

		Location location = new Location();

		Mockito.when(geolocationService.getLocation(Mockito.anyString())).thenReturn(location);

		Mockito.when(realtimeWeatherService.getByLocation(location)).thenThrow(LocationNotFoundException.class);

		mockMvc.perform(get(END_URI_PATH)).andExpect(status().isNotFound()).andDo(print());
	}

	@Test
	public void testGetShouldReturnStatus200OK() throws Exception {
		Location location = new Location();
		location.setCode("SFCA_USA");
		location.setCityName("San Franciso");
		location.setRegionName("California");
		location.setCountryName("United States of America");
		location.setCountryCode("US");

		RealtimeWeather realtimeWeather = new RealtimeWeather();
		realtimeWeather.setTemperature(12);
		realtimeWeather.setHumidity(32);
		realtimeWeather.setLastUpdated(new Date());
		realtimeWeather.setPrecipitation(88);
		realtimeWeather.setStatus("Cloudy");
		realtimeWeather.setWindSpeed(5);

		realtimeWeather.setLocation(location);
		location.setRealtimeWeather(realtimeWeather);

		Mockito.when(geolocationService.getLocation(Mockito.anyString())).thenReturn(location);
		Mockito.when(realtimeWeatherService.getByLocation(location)).thenReturn(realtimeWeather);

		String expectedLocation = location.getCityName() + ", " + location.getRegionName() + ", "
				+ location.getCountryName();

		mockMvc.perform(get(END_URI_PATH)).andExpect(status().isOk())
				.andExpect(content().contentType(RESPONSE_CONTENT_TYPE))
				.andExpect(jsonPath("$.location", is(expectedLocation)))
				.andExpect(jsonPath("$._links.self.href", is("http://localhost/v1/realtime")))
				.andExpect(jsonPath("$._links.hourly_forecast.href", is("http://localhost/v1/hourly")))
				.andExpect(jsonPath("$._links.daily_forecast.href", is("http://localhost/v1/daily")))
				.andExpect(jsonPath("$._links.full_forecast.href", is("http://localhost/v1/full"))).andDo(print());
	}

	@Test
	public void testGetByLocationCodeShouldReturnStatus404NotFound() throws Exception {

		String code = "ABC_TEST";
		String uriPath = END_URI_PATH + "/" + code;

		Mockito.when(realtimeWeatherService.getByLocationCode(code)).thenThrow(LocationNotFoundException.class);

		mockMvc.perform(get(uriPath)).andExpect(status().isNotFound()).andDo(print());

	}

	@Test
	public void testGetByLocationCodeShouldReturnStatus200OK() throws Exception {
		String locationCode ="SFCA_USA";
		String uriPath = END_URI_PATH + "/"  + locationCode;
		
		Location location = new Location();
		location.setCode("SFCA_USA");
		location.setCityName("San Franciso");
		location.setRegionName("California");
		location.setCountryName("United States of America");
		location.setCountryCode("US");
		
		RealtimeWeather realtimeWeather = new RealtimeWeather();
		realtimeWeather.setTemperature(12);
		realtimeWeather.setHumidity(32);
		realtimeWeather.setLastUpdated(new Date());
		realtimeWeather.setPrecipitation(88);
		realtimeWeather.setStatus("Cloudy");
		realtimeWeather.setWindSpeed(5);
		
		realtimeWeather.setLocation(location);
		location.setRealtimeWeather(realtimeWeather);
		

		String expectedLocation = location.getCityName() + ", " + location.getRegionName() + ", "
				+ location.getCountryName();
		
		Mockito.when(realtimeWeatherService.getByLocationCode(location.getCode())).thenReturn(realtimeWeather);
		
		    mockMvc.perform(get(uriPath))
		    .andExpect(status().isOk())
		    .andExpect(content().contentType(RESPONSE_CONTENT_TYPE))
		    .andExpect(jsonPath("$.location", is(expectedLocation)))
		    .andExpect(jsonPath("$._links.self.href", is("http://localhost/v1/realtime/" + locationCode)))
		    .andExpect(jsonPath("$._links.hourly_forecast.href", is("http://localhost/v1/hourly/" + locationCode)))
		    .andExpect(jsonPath("$._links.daily_forecast.href", is("http://localhost/v1/daily/" + locationCode)))
		    .andExpect(jsonPath("$._links.full_forecast.href", is("http://localhost/v1/full/" + locationCode)))
		    .andDo(print());
		
		
		
	}

	@Test
	public void testUpdateShouldReturn400BadRequest() throws Exception {
		String locationCode = "SFCA_USA";
		String uriPath = END_URI_PATH + "/" + locationCode;

		RealtimeWeather realtimeWeather = new RealtimeWeather();
		realtimeWeather.setTemperature(120);
		realtimeWeather.setHumidity(132);

		realtimeWeather.setPrecipitation(88);
		realtimeWeather.setStatus("Cl");
		realtimeWeather.setWindSpeed(500);

		String bodyContent = mapper.writeValueAsString(realtimeWeather);
		System.out.println(realtimeWeather);
		mockMvc.perform(put(uriPath).contentType(REQUEST_CONTENT_TYPE).content(bodyContent))
				.andExpect(status().isBadRequest()).andDo(print());
		// System.out.println(" Details :"+realtimeWeather);

	}

	@Test
	public void testUpdateShouldReturn404NotFound() throws Exception {
		String locationCode = "ABC_US";
		String requestURI = END_URI_PATH + "/" + locationCode;

		RealtimeWeatherDTO dto = new RealtimeWeatherDTO();
		dto.setTemperature(12);
		dto.setHumidity(32);
		dto.setPrecipitation(88);
		dto.setStatus("Cloudy");
		dto.setWindSpeed(5);

		LocationNotFoundException ex = new LocationNotFoundException(locationCode);
		Mockito.when(realtimeWeatherService.update(Mockito.eq(locationCode), Mockito.any())).thenThrow(ex);

		String bodyContent = mapper.writeValueAsString(dto);

		mockMvc.perform(put(requestURI).contentType(REQUEST_CONTENT_TYPE).content(bodyContent))
				.andExpect(status().isNotFound()).andExpect(jsonPath("$.errors[0]", is(ex.getMessage())))
				.andDo(print());
	}

	@Test
	public void testUpdateShouldReturn200OK() throws Exception {
		String locationCode = "APE_PK";
		String requestURI = END_URI_PATH + "/" + locationCode;

		RealtimeWeather realtimeWeather = new RealtimeWeather();
		realtimeWeather.setTemperature(12);
		realtimeWeather.setHumidity(32);
		realtimeWeather.setPrecipitation(88);
		realtimeWeather.setStatus("Cloudy");
		realtimeWeather.setWindSpeed(5);
		realtimeWeather.setLastUpdated(new Date());

		RealtimeWeatherDTO dto = new RealtimeWeatherDTO();
		dto.setTemperature(12);
		dto.setHumidity(32);
		dto.setPrecipitation(88);
		dto.setStatus("Cloudy");
		dto.setWindSpeed(5);

		Location location = new Location();
		location.setCode(locationCode);
		location.setCityName("Ahmad Pur East");
		location.setRegionName("Punjab");
		location.setCountryName("Pakistan");
		location.setCountryCode("PK");

		realtimeWeather.setLocation(location);
		location.setRealtimeWeather(realtimeWeather);

		Mockito.when(realtimeWeatherService.update(locationCode, realtimeWeather)).thenReturn(realtimeWeather);

		String bodyContent = mapper.writeValueAsString(dto);

		String expectedLocation = location.getCityName() + ", " + location.getRegionName() + ", "
				+ location.getCountryName();

		mockMvc.perform(put(requestURI).contentType(REQUEST_CONTENT_TYPE).content(bodyContent))
				.andExpect(status().isOk()).andExpect(content().contentType(RESPONSE_CONTENT_TYPE))
				.andExpect(jsonPath("$.location", is(expectedLocation)))
				.andExpect(jsonPath("$._links.self.href", is("http://localhost/v1/realtime/" + locationCode)))
				.andExpect(jsonPath("$._links.hourly_forecast.href", is("http://localhost/v1/hourly/" + locationCode)))
				.andExpect(jsonPath("$._links.daily_forecast.href", is("http://localhost/v1/daily/" + locationCode)))
				.andExpect(jsonPath("$._links.full_forecast.href", is("http://localhost/v1/full/" + locationCode)))
				.andExpect(jsonPath("$.location", is(expectedLocation)))
				.andDo(print());
	

	}

}
