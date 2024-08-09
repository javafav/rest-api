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
	@Autowired  MockMvc mockMvc;
	@Autowired  ObjectMapper mapper;
	
	@MockBean GeolocationService geolocationService;
	@MockBean RealtimeWeatherService realtimeWeatherService;
	
	@Test
	public void testGetShouldReturn400BadReqest() throws Exception {
		
		Mockito.when(
				geolocationService.getLocation(anyString())).thenThrow(GeoLocationException.class);
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
		
		String expectedLocation = location.getCityName() + ", " + location.getRegionName() + ", " + location.getCountryName();
		
		mockMvc.perform(get(END_URI_PATH))
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
			//	.andExpect(jsonPath("$.location", is(expectedLocation)))
				
				.andDo(print());		
	}
	
	@Test
	public void testGetByLocationCodeShouldReturnStatus404NotFound() throws Exception {
		
		String code ="ABC_TEST";
		String uriPath = END_URI_PATH + "/"  + code;
		
		Mockito.when(realtimeWeatherService.getByLocationCode(code)).thenThrow(LocationNotFoundException.class);
		
		mockMvc.perform(get(uriPath)).andExpect(status().isNotFound()).andDo(print());
		
		
		
	}
	
	@Test
	public void testGetByLocationCodeShouldReturnStatus200OK() throws Exception {
		String code ="SFCA_USA";
		String uriPath = END_URI_PATH + "/"  + code;
		
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
		
		Mockito.when(realtimeWeatherService.getByLocationCode(location.getCode())).thenReturn(realtimeWeather);
		
		mockMvc.perform(get(uriPath)).andExpect(status().isOk()).andDo(print());
		
		
		
	}
	
	
	@Test
	public void testUpdateShouldReturn400BadRequest() throws Exception {
		String locationCode ="SFCA_USA";
		String uriPath = END_URI_PATH + "/"  + locationCode;
		
		RealtimeWeather realtimeWeather = new RealtimeWeather();
		realtimeWeather.setTemperature(120);
		realtimeWeather.setHumidity(132);

		realtimeWeather.setPrecipitation(88);
		realtimeWeather.setStatus("Cl");
		realtimeWeather.setWindSpeed(500);
		
		
		
		String bodyContent = mapper.writeValueAsString(realtimeWeather);
		System.out.println(realtimeWeather);
		mockMvc.perform(put(uriPath).contentType("application/json").content(bodyContent)).andExpect(status().isBadRequest()).andDo(print());
		//System.out.println(" Details :"+realtimeWeather);
		
		
	}
	
	@Test
	public void testUpdateShouldReturn40NotFound() throws Exception {
		String locationCode ="SFCA_USA";
		String uriPath = END_URI_PATH + "/"  + locationCode;
	
		
		RealtimeWeather realtimeWeather = new RealtimeWeather();
		realtimeWeather.setTemperature(12);
		realtimeWeather.setHumidity(32);
		realtimeWeather.setLastUpdated(new Date());
		realtimeWeather.setPrecipitation(88);
		realtimeWeather.setStatus("Cloudy");
		realtimeWeather.setWindSpeed(5);
		
		
		Mockito.when(realtimeWeatherService.update(locationCode, realtimeWeather)).thenThrow(LocationNotFoundException.class);
		
		
		
		String bodyContent = mapper.writeValueAsString(realtimeWeather);
		System.out.println(realtimeWeather);
		mockMvc.perform(put(uriPath).contentType("application/json").content(bodyContent)).andExpect(status().isNotFound()).andDo(print());
		//System.out.println(" Details :"+realtimeWeather);
		
		
	}
	
	
	@Test
	public void testUpdateShouldReturn200OK() throws Exception {
		String locationCode ="SFCA_USA";
		String uriPath = END_URI_PATH + "/"  + locationCode;
	
		
		RealtimeWeather realtimeWeather = new RealtimeWeather();
		realtimeWeather.setTemperature(12);
		realtimeWeather.setHumidity(32);
		realtimeWeather.setPrecipitation(88);
		realtimeWeather.setStatus("Cloudy");
		realtimeWeather.setWindSpeed(5);
		realtimeWeather.setLastUpdated(new Date());
		
		Location location = new Location();
		location.setCode(locationCode);
		location.setCityName("Uch Sharif");
		location.setRegionName("Bahwalpur");
		location.setCountryCode("PK");
		location.setCountryName("Pakistan");
		
		location.setRealtimeWeather(realtimeWeather);
		realtimeWeather.setLocation(location);
		
		
		Mockito.when(realtimeWeatherService.update(locationCode, realtimeWeather)).thenReturn(realtimeWeather);
		
		
		
		String bodyContent = mapper.writeValueAsString(realtimeWeather);
		String expectedLocation = location.getCityName() + ", " + location.getRegionName() + ", " + location.getCountryName();

		System.out.println(realtimeWeather);
		mockMvc.perform(put(uriPath).contentType("application/json").content(bodyContent)).
		andExpect(jsonPath("$.location", is(expectedLocation)))
		.andExpect(status().isOk()).andDo(print());
		//System.out.println(" Details :"+realtimeWeather);
		
		
	}
	

}
