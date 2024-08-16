package com.skyapi.weatherforecast.hourly;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skyapi.weatherforecast.GeoLocationException;
import com.skyapi.weatherforecast.GeolocationService;
import com.skyapi.weatherforecast.common.HourlyWeather;
import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.location.LocationNotFoundException;

@WebMvcTest(HourlyWeatherApiController.class)
public class HourlyWeatherApiControllerTests {

	private static final String END_URI_PATH = "/v1/hourly";
	private static final String X_CURRENT_HOUR = "X-Current-Hour";
	
	@Autowired private MockMvc mockMvc;
	@Autowired private ObjectMapper objectMapper;
	@MockBean private GeolocationService locationService;
	@MockBean private HourlyWeatherService hourlyWeatherService;
	
	@Test
	public void testGetByIPAddresShouldReturn400BadRequestBeacuseNoHeaderXCurrentHour() throws Exception {
		
		mockMvc.perform(get(END_URI_PATH))
		            .andExpect(status().isBadRequest()).andDo(print());
	}
	
	@Test
	public void testtestGetByIPAddresShouldReturn400BadRequestBeacuseGeoLocationException() throws Exception {
		Mockito.when( locationService.getLocation(Mockito.anyString()))
		       .thenThrow(GeoLocationException.class); 
		
		mockMvc.perform(get(END_URI_PATH).header("X-Forword-Hour", 7))
        .andExpect(status().isBadRequest()).andDo(print());
		
	}
	
	@Test
	public void testGetByIPAddresShouldReturn204NoContent() throws Exception {
		int currentHour = 9;
		Location location = new Location().code("KHI_PK");
		
		
		Mockito.when( locationService.getLocation(Mockito.anyString())).thenReturn(location);
		Mockito.when(hourlyWeatherService.getByLocation(location, currentHour)).thenReturn(new ArrayList<>());
		
		mockMvc.perform(get(END_URI_PATH).header("X-Forword-Hour", String.valueOf(currentHour)))
        .andExpect(status().isNoContent()).andDo(print());
		
	}
	
	@Test
	public void testGetByIPAddresShouldReturn200OK() throws Exception {
		int currentHour = 9;
	
		Location location = new Location();
		location.setCode("UCH_PK");
		location.setCityName("Uch Sharif");
		location.setRegionName("Bahwalpur");
		location.setCountryCode("PK");
		location.setCountryName("Pakistan");
	
		
		HourlyWeather forecast1 = new HourlyWeather().id(9, location)
                .precipitation(40)
                .temperature(32)
                .status("Sunny");
		
		
		
		HourlyWeather forecast2 = new HourlyWeather().location(location).hourOfDay(10)
                .precipitation(38)
                .temperature(30)
                .status("Cloudy");
		
		Mockito.when( locationService.getLocation(Mockito.anyString())).thenReturn(location);
		Mockito.when(hourlyWeatherService.getByLocation(location, currentHour)).thenReturn(List.of(forecast1, forecast2));
		
		mockMvc.perform(get(END_URI_PATH).header("X-Forword-Hour", String.valueOf(currentHour)))
		.andExpect(jsonPath("$.location", is(location.toString())))
		.andExpect(jsonPath("$.hourly_forecast[0].hour_of_day", is(9)))
        .andExpect(status().isOk()).andDo(print());
		
	}
	
	@Test
	public void testGetByCodeShouldReturn400BadRequestBeacuseNoHeaderXCurrentHour() throws Exception {
		String code = "UCH_PK";
		String requestURI = END_URI_PATH + "/" + code;
		mockMvc.perform(get(requestURI))
		            .andExpect(status().isBadRequest()).andDo(print());
	}
	
	
	@Test
	public void testGetByCodeShouldReturn404NotFound() throws Exception {
		int currentHour = 9;
		String locationCode = "DELHI_IN";
		String requestURI = END_URI_PATH  + "/" + locationCode;
		
		LocationNotFoundException ex = new LocationNotFoundException(locationCode);
		when(hourlyWeatherService.getByLocationCode(locationCode, currentHour)).thenThrow(ex);
		
		mockMvc.perform(get(requestURI).header(X_CURRENT_HOUR, String.valueOf(currentHour)))
				.andExpect(status().isNotFound())
				.andDo(print());
	}
	
	@Test
	public void testGetByCodeShouldReturn204NoContent() throws Exception {
		int currentHour = 9;
		String locationCode = "DELHI_IN";
		String requestURI = END_URI_PATH  + "/" + locationCode;
		
		when(hourlyWeatherService.getByLocationCode(locationCode, currentHour)).thenReturn(Collections.emptyList());
		
		mockMvc.perform(get(requestURI).header(X_CURRENT_HOUR, String.valueOf(currentHour)))
				.andExpect(status().isNoContent())
				.andDo(print());
	}	
	
	@Test
	public void testGetByCodeShouldReturn200OK() throws Exception {
		int currentHour = 9;
		String locationCode = "DELHI_IN";
		String requestURI = END_URI_PATH + "/" + locationCode;
		
		Location location = new Location();
		location.setCode(locationCode);
		location.setCityName("New York City");
		location.setRegionName("New York");
		location.setCountryCode("US");
		location.setCountryName("United States of America");
		
		HourlyWeather forecast1 = new HourlyWeather()
				.location(location)
				.hourOfDay(10)
				.temperature(13)
				.precipitation(70)
				.status("Cloudy");		
		
		HourlyWeather forecast2 = new HourlyWeather()
				.location(location)
				.hourOfDay(11)
				.temperature(15)
				.precipitation(60)
				.status("Sunny");			
		
		var hourlyForecast = List.of(forecast1, forecast2);
		
		when(hourlyWeatherService.getByLocationCode(locationCode, currentHour)).thenReturn(hourlyForecast);
		
		mockMvc.perform(get(requestURI).header(X_CURRENT_HOUR, String.valueOf(currentHour)))
				.andExpect(status().isOk())
				.andExpect(content().contentType("application/json"))
				.andExpect(jsonPath("$.location", is(location.toString())))
								
				.andDo(print());
	}	
	
	@Test
	public void testUpdateShouldReturn400BadRequestBecauseNoData() throws Exception {
		
		String locationCode = "DELHI_IN";
		String requestURI = END_URI_PATH + "/" + locationCode;
		
		List<HourlyWeatherDTO> listDTO = Collections.emptyList();
		
		String bodyContent = objectMapper.writeValueAsString(listDTO);
		
		mockMvc.perform(put(requestURI).contentType(MediaType.APPLICATION_JSON).content(bodyContent))
		.andExpect(jsonPath("$.errors[0]", is("Hourly forecast data cannot be empty")))                        
		.andExpect(status().isBadRequest())
		.andDo(print());
	}
	
	@Test
	public void testUpdateShouldReturn400BadRequestBecauseInvlaidData() throws Exception {
		
		String locationCode = "DELHI_IN";
		String requestURI = END_URI_PATH + "/" + locationCode;
		
		HourlyWeatherDTO dto1 = new HourlyWeatherDTO()
				.hourOfDay(100)
				.temperature(130)
				.precipitation(700)
				.status("Cloudy");		
		
		HourlyWeatherDTO dto2 = new HourlyWeatherDTO()
				.hourOfDay(110)
				.temperature(150)
				.precipitation(610)
				.status("");	
		
		 List<HourlyWeatherDTO> listDTO = List.of(dto1,dto2);
		String bodyContent = objectMapper.writeValueAsString(listDTO);
		
		mockMvc.perform(put(requestURI).contentType(MediaType.APPLICATION_JSON).content(bodyContent))                       
		.andExpect(status().isBadRequest())
		.andDo(print());
	}
	
	@Test
	public void testUpdateShouldReturn404NotFound() throws Exception {
		
		String locationCode = "DELHI_IN";
		String requestURI = END_URI_PATH + "/" + locationCode;
		
		HourlyWeatherDTO dto1 = new HourlyWeatherDTO()
				.hourOfDay(100)
				.temperature(10)
				.precipitation(70)
				.status("Cloudy");		
		
		
		 List<HourlyWeatherDTO> listDTO = List.of(dto1);
		when(hourlyWeatherService.updateByLocationCode(Mockito.eq(locationCode), Mockito.anyList())).thenThrow(LocationNotFoundException.class);
		 
		 
		 
		 String bodyContent = objectMapper.writeValueAsString(listDTO);
		
		mockMvc.perform(put(requestURI).contentType(MediaType.APPLICATION_JSON).content(bodyContent))                       
		.andExpect(status().isNotFound())
		.andDo(print());
	}
	
	@Test
	public void testUpdateShouldReturn200OOK() throws Exception {
		
		String locationCode = "DELHI_IN";
		String requestURI = END_URI_PATH + "/" + locationCode;
		
		HourlyWeatherDTO dto1 = new HourlyWeatherDTO()
				.hourOfDay(100)
				.temperature(10)
				.precipitation(70)
				.status("Cloudy");	
		
		Location location = new Location();
		location.setCode(locationCode);
		location.setCityName("New York City");
		location.setRegionName("New York");
		location.setCountryCode("US");
		location.setCountryName("United States of America");
		
		HourlyWeather forecast1 = new HourlyWeather()
				.location(location)
				.hourOfDay(10)
				.temperature(13)
				.precipitation(70)
				.status("Cloudy");	
		
		
		 List<HourlyWeatherDTO> listDTO = List.of(dto1);
		 List<HourlyWeather> hourlyForecast = List.of(forecast1);
		 
		 when(hourlyWeatherService.updateByLocationCode(Mockito.eq(locationCode), Mockito.anyList())).thenReturn(hourlyForecast);
		 
		 
		 
		 String bodyContent = objectMapper.writeValueAsString(listDTO);
		
		mockMvc.perform(put(requestURI).contentType(MediaType.APPLICATION_JSON).content(bodyContent))                       
		.andExpect(status().isOk())
	    .andExpect(jsonPath("$.location", is(location.toString()))) 
	    .andExpect(jsonPath("$.hourly_forecast[0].hour_of_day", is(10))) 
		.andDo(print());
	}
	
	
}
