package com.skyapi.weatherforecast.hourly;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.skyapi.weatherforecast.common.HourlyWeather;

import jakarta.validation.constraints.NotBlank;

@JsonPropertyOrder({"hour_of_day", "temperature", "precipitation", "status "})
public class HourlyWeatherDTO {

	@JsonProperty("hour_of_day")
	private int hourOfDay;
	
	@Range(min = -50,max = 50, message = "Temperature must be in the range of -50 to 50 Celsius degree")
	private int temperature;
	
	@Range(min = 0, max = 100, message = "Precipitation must be in the range of 0 to 100 percentage")
	private int precipitation;
	
	@NotBlank(message = "Status must be not empty")
	@Length(min = 3, max = 50, message = "Status must be in the 3 to 50 chracters")
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

	@Override
	public String toString() {
		return "HourlyWeatherDTO [hourOfDay=" + hourOfDay + ", temperature=" + temperature + ", precipitation="
				+ precipitation + ", status=" + status + "]";
	}


}
