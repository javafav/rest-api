package com.skyapi.weatherforecast;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.skyapi.weatherforecast.common.DailyWeather;
import com.skyapi.weatherforecast.common.HourlyWeather;
import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.common.RealtimeWeather;
import com.skyapi.weatherforecast.daily.DailyWeatherDTO;
import com.skyapi.weatherforecast.full.FullWeatherApiController;
import com.skyapi.weatherforecast.full.FullWeatherDTO;
import com.skyapi.weatherforecast.full.FullWeatherService;
import com.skyapi.weatherforecast.hourly.HourlyWeatherDTO;
import com.skyapi.weatherforecast.location.LocationNotFoundException;
import com.skyapi.weatherforecast.realtime.RealtimeWeatherDTO;

@WebMvcTest(FullWeatherApiController.class)
public class FullWeatherApiControllerTests {

	private static final String END_POINT_PATH = "/v1/full";
	private static final String RESPONSE_CONTENT_TYPE = "application/json";
	private static final String REQUEST_CONTENT_TYPE = "application/json";

	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private FullWeatherService weatherService;
	@MockBean
	private GeolocationService locationService;

	@Test
	public void testGetByIPShouldReturn400BadRequestBecauseGeolocationException() throws Exception {
		GeoLocationException ex = new GeoLocationException("Geolocation error");
		Mockito.when(locationService.getLocation(Mockito.anyString())).thenThrow(ex);

		mockMvc.perform(get(END_POINT_PATH)).andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors[0]", is(ex.getMessage()))).andDo(print());
	}

	@Test
	public void testGetByIPShouldReturn404NotFound() throws Exception {
		Location location = new Location().code("ISB_PK");

		when(locationService.getLocation(Mockito.anyString())).thenReturn(location);

		LocationNotFoundException ex = new LocationNotFoundException(location.getCode());
		when(weatherService.getByLocation(location)).thenThrow(ex);

		mockMvc.perform(get(END_POINT_PATH)).andExpect(status().isNotFound())
				.andExpect(jsonPath("$.errors[0]", is(ex.getMessage()))).andDo(print());
	}

	@Test
	public void testGetByIPShouldReturn200OK() throws Exception {
		Location location = new Location();
		location.setCode("NYC_USA");
		location.setCityName("New York City");
		location.setRegionName("New York");
		location.setCountryCode("US");
		location.setCountryName("United States of America");

		RealtimeWeather realtimeWeather = new RealtimeWeather();
		realtimeWeather.setTemperature(12);
		realtimeWeather.setHumidity(32);
		realtimeWeather.setLastUpdated(new Date());
		realtimeWeather.setPrecipitation(88);
		realtimeWeather.setStatus("Cloudy");
		realtimeWeather.setWindSpeed(5);

		location.setRealtimeWeather(realtimeWeather);

		DailyWeather dailyForecast1 = new DailyWeather().location(location).dayOfMonth(16).month(7).minTemp(23)
				.maxTemp(32).precipitation(40).status("Cloudy");

		DailyWeather dailyForecast2 = new DailyWeather().location(location).dayOfMonth(17).month(7).minTemp(25)
				.maxTemp(34).precipitation(30).status("Sunny");

		HourlyWeather hourlyForecast1 = new HourlyWeather().id(9, location).precipitation(40).temperature(32)
				.status("Sunny");

		HourlyWeather hourlyForecast2 = new HourlyWeather().location(location).hourOfDay(10).precipitation(38)
				.temperature(30).status("Cloudy");

		location.setListDailyWeather(List.of(dailyForecast1, dailyForecast2));
		location.setListHourlyWeather(List.of(hourlyForecast1, hourlyForecast2));

		Mockito.when(locationService.getLocation(Mockito.anyString())).thenReturn(location);
		when(weatherService.getByLocation(location)).thenReturn(location);

		String expectedLocation = location.toString();

		mockMvc.perform(get(END_POINT_PATH)).andExpect(status().isOk())
				.andExpect(content().contentType(RESPONSE_CONTENT_TYPE))
				.andExpect(jsonPath("$.location", is(expectedLocation)))
				.andExpect(jsonPath("$.daily_forecast[0].day_of_month", is(16)))
				.andExpect(jsonPath("$.realtime_weather.temperature", is(12)))
				.andExpect(jsonPath("$.hourly_forecast[0].hour_of_day", is(9))).andDo(print());
	}

	@Test
	public void testGetByCodeShouldReturn404NotFound() throws Exception {
		String locationCode = "ABC123";
		String requestURI = END_POINT_PATH + "/" + locationCode;

		LocationNotFoundException ex = new LocationNotFoundException(locationCode);
		when(weatherService.get(locationCode)).thenThrow(ex);

		mockMvc.perform(get(requestURI)).andExpect(status().isNotFound())
				.andExpect(jsonPath("$.errors[0]", is(ex.getMessage()))).andDo(print());
	}

	@Test
	public void testGetByCodeShouldReturn200OK() throws Exception {
		String locationCode = "NYC_USA";
		String requestURI = END_POINT_PATH + "/" + locationCode;

		Location location = new Location();
		location.setCode(locationCode);
		location.setCityName("New York City");
		location.setRegionName("New York");
		location.setCountryCode("US");
		location.setCountryName("United States of America");

		RealtimeWeather realtimeWeather = new RealtimeWeather();
		realtimeWeather.setTemperature(12);
		realtimeWeather.setHumidity(32);
		realtimeWeather.setLastUpdated(new Date());
		realtimeWeather.setPrecipitation(88);
		realtimeWeather.setStatus("Cloudy");
		realtimeWeather.setWindSpeed(5);

		location.setRealtimeWeather(realtimeWeather);

		DailyWeather dailyForecast1 = new DailyWeather().location(location).dayOfMonth(16).month(7).minTemp(23)
				.maxTemp(32).precipitation(40).status("Cloudy");

		DailyWeather dailyForecast2 = new DailyWeather().location(location).dayOfMonth(17).month(7).minTemp(25)
				.maxTemp(34).precipitation(30).status("Sunny");

		location.setListDailyWeather(List.of(dailyForecast1, dailyForecast2));

		HourlyWeather hourlyForecast1 = new HourlyWeather().location(location).hourOfDay(10).temperature(13)
				.precipitation(70).status("Cloudy");

		HourlyWeather hourlyForecast2 = new HourlyWeather().location(location).hourOfDay(11).temperature(15)
				.precipitation(60).status("Sunny");

		location.setListHourlyWeather(List.of(hourlyForecast1, hourlyForecast2));

		when(weatherService.get(locationCode)).thenReturn(location);

		String expectedLocation = location.toString();

		mockMvc.perform(get(requestURI)).andExpect(status().isOk())
				.andExpect(content().contentType(RESPONSE_CONTENT_TYPE))
				.andExpect(jsonPath("$.location", is(expectedLocation)))
				.andExpect(jsonPath("$.realtime_weather.temperature", is(12)))
				.andExpect(jsonPath("$.hourly_forecast[0].hour_of_day", is(10)))
				.andExpect(jsonPath("$.daily_forecast[0].precipitation", is(40))).andDo(print());
	}

	@Test
	public void testGetByIPShouldReturn400BadRequestBecauseHourlyDataEmpty() throws Exception {
		String locationCode = "BWP_PK";
		String requestURI = END_POINT_PATH + "/" + locationCode;

		FullWeatherDTO dto = new FullWeatherDTO();

		String requestBody = objectMapper.writeValueAsString(dto);

		mockMvc.perform(put(requestURI).contentType("application/json").content(requestBody))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors[0]", is("Hourly weather data cannot be empty"))).andDo(print());
	}

	@Test
	public void testGetByIPShouldReturn400BadRequestBecauseDailyDataEmpty() throws Exception {
		String locationCode = "BWP_PK";
		String requestURI = END_POINT_PATH + "/" + locationCode;

		HourlyWeatherDTO hourlyForecast = new HourlyWeatherDTO().hourOfDay(11).temperature(15).precipitation(60)
				.status("Sunny");

	

		FullWeatherDTO dto = new FullWeatherDTO();
		dto.getListHourlyWeather().add(hourlyForecast);
		

		String requestBody = objectMapper.writeValueAsString(dto);

		mockMvc.perform(put(requestURI).contentType("application/json").content(requestBody))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors[0]", is("Daily weather data cannot be empty"))).andDo(print());
	}

	@Test
	public void testUpdateShouldReturn400BadRequestBecauseInvalidRealtimeWeatherData() throws Exception {
		String locationCode = "NYC_USA";
		String requestURI = END_POINT_PATH + "/" + locationCode;

		FullWeatherDTO fullWeatherDTO = new FullWeatherDTO();

		HourlyWeatherDTO hourlyForecast1 = new HourlyWeatherDTO().hourOfDay(10).temperature(13).precipitation(70)
				.status("Cloudy");

		fullWeatherDTO.getListHourlyWeather().add(hourlyForecast1);

		DailyWeatherDTO dailyForecast1 = new DailyWeatherDTO().dayOfMonth(17).month(7).minTemp(25).maxTemp(34)
				.precipitation(30).status("Sunny");

		fullWeatherDTO.getListDailyWeather().add(dailyForecast1);

		RealtimeWeatherDTO realtimeDTO = new RealtimeWeatherDTO();
		realtimeDTO.setTemperature(122);
		realtimeDTO.setHumidity(20);
		realtimeDTO.setLastUpdated(new Date());
		realtimeDTO.setPrecipitation(88);
		realtimeDTO.setStatus("Cloudy");
		realtimeDTO.setWindSpeed(5);

		fullWeatherDTO.setRealtimeWeather(realtimeDTO);

		String requestBody = objectMapper.writeValueAsString(fullWeatherDTO);

		mockMvc.perform(put(requestURI).contentType("application/json").content(requestBody))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors[0]", containsString("Temperature must be in the range"))).andDo(print());
	}

	@Test
	public void testUpdateShouldReturn400BadRequestBecauseInvalidHoulryWeatherData() throws Exception {
		String locationCode = "NYC_USA";
		String requestURI = END_POINT_PATH + "/" + locationCode;

		FullWeatherDTO fullWeatherDTO = new FullWeatherDTO();

		HourlyWeatherDTO hourlyForecast1 = new HourlyWeatherDTO().hourOfDay(100).temperature(13)

				.precipitation(70).status("Cloudy");

		fullWeatherDTO.getListHourlyWeather().add(hourlyForecast1);

		DailyWeatherDTO dailyForecast1 = new DailyWeatherDTO().dayOfMonth(17).month(7).minTemp(25).maxTemp(34)
				.precipitation(30).status("Sunny");

		fullWeatherDTO.getListDailyWeather().add(dailyForecast1);

		RealtimeWeatherDTO realtimeDTO = new RealtimeWeatherDTO();
		realtimeDTO.setTemperature(12);
		realtimeDTO.setHumidity(20);
		realtimeDTO.setLastUpdated(new Date());
		realtimeDTO.setPrecipitation(88);
		realtimeDTO.setStatus("Cloudy");
		realtimeDTO.setWindSpeed(5);

		fullWeatherDTO.setRealtimeWeather(realtimeDTO);

		String requestBody = objectMapper.writeValueAsString(fullWeatherDTO);

		mockMvc.perform(put(requestURI).contentType("application/json").content(requestBody))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors[0]", containsString("Hour must be in between 0-23"))).andDo(print());
	}

	@Test
	public void testUpdateShouldReturn400BadRequestBecauseInvalidDailyWeatherData() throws Exception {
		String locationCode = "NYC_USA";
		String requestURI = END_POINT_PATH + "/" + locationCode;

		Location location = new Location();
		location.setCode(locationCode);

		FullWeatherDTO fullWeatherDTO = new FullWeatherDTO();

		HourlyWeatherDTO hourlyForecast1 = new HourlyWeatherDTO().hourOfDay(10).temperature(13)

				.precipitation(70).status("Cloudy");

		fullWeatherDTO.getListHourlyWeather().add(hourlyForecast1);

		DailyWeatherDTO dailyForecast1 = new DailyWeatherDTO().dayOfMonth(17).month(70).minTemp(25).maxTemp(34)
				.precipitation(30).status("Sunny");

		fullWeatherDTO.getListDailyWeather().add(dailyForecast1);

		RealtimeWeatherDTO realtimeDTO = new RealtimeWeatherDTO();
		realtimeDTO.setTemperature(12);
		realtimeDTO.setHumidity(20);
		realtimeDTO.setLastUpdated(new Date());
		realtimeDTO.setPrecipitation(88);
		realtimeDTO.setStatus("Cloudy");
		realtimeDTO.setWindSpeed(5);

		fullWeatherDTO.setRealtimeWeather(realtimeDTO);

		String requestBody = objectMapper.writeValueAsString(fullWeatherDTO);

		mockMvc.perform(put(requestURI).contentType("application/json").content(requestBody))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.errors[0]", containsString("Month must be between 1-12"))).andDo(print());
	}

	@Test
	public void testUpdateShouldReturn404NotFound() throws Exception {
		String locationCode = "NYC_USA";
		String requestURI = END_POINT_PATH + "/" + locationCode;

		Location location = new Location();
		location.setCode(locationCode);

		FullWeatherDTO fullWeatherDTO = new FullWeatherDTO();

		HourlyWeatherDTO hourlyForecast1 = new HourlyWeatherDTO().hourOfDay(17).temperature(13)

				.precipitation(70).status("Cloudy");

		fullWeatherDTO.getListHourlyWeather().add(hourlyForecast1);

		DailyWeatherDTO dailyForecast1 = new DailyWeatherDTO().dayOfMonth(17).month(7).minTemp(25).maxTemp(34)
				.precipitation(30).status("Sunny");

		fullWeatherDTO.getListDailyWeather().add(dailyForecast1);

		RealtimeWeatherDTO realtimeDTO = new RealtimeWeatherDTO();
		realtimeDTO.setTemperature(12);
		realtimeDTO.setHumidity(20);
		realtimeDTO.setLastUpdated(new Date());
		realtimeDTO.setPrecipitation(88);
		realtimeDTO.setStatus("Cloudy");
		realtimeDTO.setWindSpeed(5);

		fullWeatherDTO.setRealtimeWeather(realtimeDTO);

		LocationNotFoundException ex = new LocationNotFoundException("No location found");

		when(weatherService.update(Mockito.eq(locationCode), Mockito.any())).thenThrow(ex);

		String requestBody = objectMapper.writeValueAsString(fullWeatherDTO);

		mockMvc.perform(put(requestURI).contentType("application/json").content(requestBody))
				.andExpect(status().isNotFound())
				// .andExpect(jsonPath("$.errors[0]", containsString("Hour must be in between
				// 0-23")))
				.andDo(print());
	}

	@Test
	public void testUpdateShouldReturn200OK() throws Exception {
		String locationCode = "NYC_USA";
		String requestURI = END_POINT_PATH + "/" + locationCode;

		Location location = new Location();
		location.setCode(locationCode);
		location.setCityName("New York City");
		location.setRegionName("New York");
		location.setCountryCode("US");
		location.setCountryName("United States of America");

		RealtimeWeather realtimeWeather = new RealtimeWeather();
		realtimeWeather.setTemperature(12);
		realtimeWeather.setHumidity(32);
		realtimeWeather.setLastUpdated(new Date());
		realtimeWeather.setPrecipitation(88);
		realtimeWeather.setStatus("Cloudy");
		realtimeWeather.setWindSpeed(5);

		location.setRealtimeWeather(realtimeWeather);

		DailyWeather dailyForecast1 = new DailyWeather().location(location).dayOfMonth(16).month(7).minTemp(23)
				.maxTemp(32).precipitation(40).status("Cloudy");

		location.setListDailyWeather(List.of(dailyForecast1));

		HourlyWeather hourlyForecast1 = new HourlyWeather().location(location).hourOfDay(10).temperature(13)
				.precipitation(70).status("Cloudy");

		location.setListHourlyWeather(List.of(hourlyForecast1));

		FullWeatherDTO fullWeatherDTO = new FullWeatherDTO();

		HourlyWeatherDTO hourlyForecastDTO1 = new HourlyWeatherDTO().hourOfDay(10).temperature(13).precipitation(70)
				.status("Cloudy");

		fullWeatherDTO.getListHourlyWeather().add(hourlyForecastDTO1);

		DailyWeatherDTO dailyForecastDTO1 = new DailyWeatherDTO().dayOfMonth(16).month(7).minTemp(23).maxTemp(32)
				.precipitation(40).status("Cloudy");

		fullWeatherDTO.getListDailyWeather().add(dailyForecastDTO1);

		RealtimeWeatherDTO realtimeDTO = new RealtimeWeatherDTO();
		realtimeDTO.setTemperature(12);
		realtimeDTO.setHumidity(32);
		realtimeDTO.setLastUpdated(new Date());
		realtimeDTO.setPrecipitation(88);
		realtimeDTO.setStatus("Cloudy");
		realtimeDTO.setWindSpeed(5);

		fullWeatherDTO.setRealtimeWeather(realtimeDTO);

		String requestBody = objectMapper.writeValueAsString(fullWeatherDTO);

		when(weatherService.update(Mockito.eq(locationCode), Mockito.any())).thenReturn(location);

		mockMvc.perform(put(requestURI).contentType(REQUEST_CONTENT_TYPE).content(requestBody))
				.andExpect(status().isOk()).andExpect(content().contentType(RESPONSE_CONTENT_TYPE))
				.andExpect(jsonPath("$.realtime_weather.temperature", is(12)))
				.andExpect(jsonPath("$.hourly_forecast[0].hour_of_day", is(10)))
				.andExpect(jsonPath("$.daily_forecast[0].precipitation", is(40))).andDo(print());
	}

}
