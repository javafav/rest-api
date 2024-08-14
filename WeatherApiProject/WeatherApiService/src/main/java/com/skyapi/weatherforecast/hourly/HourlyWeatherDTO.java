package com.skyapi.weatherforecast.hourly;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.skyapi.weatherforecast.common.HourlyWeather;

@JsonPropertyOrder({"hour_of_day", "temperature", "precipitation", "status "})
public class HourlyWeatherDTO {

	@JsonProperty("hour_of_day")
	private int hourOfDay;
	private int temperature;
	private int precipitation;
	private String status;

	public int getHourOfDay() {
		return hourOfDay;
	}

	public void setHourOfDay(int hourOfDay) {
		this.hourOfDay = hourOfDay;
	}

	public int getTemperature() {
		return temperature;
	}

	public void setTemperature(int temperature) {
		this.temperature = temperature;
	}

	public int getPrecipitation() {
		return precipitation;
	}

	public void setPrecipitation(int precipitation) {
		this.precipitation = precipitation;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
	public HourlyWeatherDTO status(String status) {
		setStatus(status);
		return this;
		
	}
	
	public HourlyWeatherDTO hourOfDay(int hourOfDay) {
		setHourOfDay(hourOfDay);
		return this;
	}
	
	public HourlyWeatherDTO precipitation(int precipitation) {
		setPrecipitation(precipitation);
		return this;
	}
	public HourlyWeatherDTO temperature(int temperature) {
		setTemperature(temperature);
		return this;
	}


}
