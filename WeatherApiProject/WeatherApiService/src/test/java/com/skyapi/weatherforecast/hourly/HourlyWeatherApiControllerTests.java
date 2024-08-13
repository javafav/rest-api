package com.skyapi.weatherforecast.hourly;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.skyapi.weatherforecast.GeoLocationException;
import com.skyapi.weatherforecast.GeolocationService;
import com.skyapi.weatherforecast.common.HourlyWeather;
import com.skyapi.weatherforecast.common.Location;

@WebMvcTest(HourlyWeatherApiController.class)
public class HourlyWeatherApiControllerTests {

	private static final String END_URI_PATH = "/v1/hourly";
	
	@Autowired private MockMvc mockMvc;
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
}
