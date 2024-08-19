package com.skyapi.weatherforecast.hourly;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.skyapi.weatherforecast.common.Location;

public class HourlyWeatherListDTO {

	private String location;

	@JsonProperty("hourly_forecast")
	private List<HourlyWeatherDTO> hourlyForecast = new ArrayList<>();

	public List<HourlyWeatherDTO> getHourlyForecast() {
		return hourlyForecast;
	}

	public void setHourlyForecast(List<HourlyWeatherDTO> hourlyForecast) {
		this.hourlyForecast = hourlyForecast;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public void add(HourlyWeatherDTO dto) {
		hourlyForecast.add(dto);
	}

}
