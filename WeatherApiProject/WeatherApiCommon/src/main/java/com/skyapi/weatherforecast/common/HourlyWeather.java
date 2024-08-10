package com.skyapi.weatherforecast.common;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "hourly_weather")
public class HourlyWeather {
	
	@EmbeddedId
	private HourlyWeatherId hourlyWeatherId = new HourlyWeatherId();
	
	private int temperature;
	private int precipitation;
	
	@Column(length = 50)
	private String status;

	public HourlyWeatherId getHourlyWeatherId() {
		return hourlyWeatherId;
	}

	public void setHourlyWeatherId(HourlyWeatherId hourlyWeatherId) {
		this.hourlyWeatherId = hourlyWeatherId;
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
	
	public HourlyWeather temperature(int temperature) {
		setTemperature(temperature);
		return this;
	}
	
	public HourlyWeather precipitation(int precipitation) {
		setPrecipitation(precipitation);
		return this;
	}
	
	public HourlyWeather status(String status) {
		setStatus(status);
		return this;
		
	}
	
	public HourlyWeather hourOfDay(int hourOfDay) {
		this.hourlyWeatherId.setHourOfDay(hourOfDay);
		
		return this;
	}

	public HourlyWeather location(Location location) {
		this.hourlyWeatherId.setLocation(location);
		return this;
	}
	
	public HourlyWeather id(int hourOfDay, Location location) {
		this.hourlyWeatherId.setHourOfDay(hourOfDay);
		this.hourlyWeatherId.setLocation(location);
		return this;
	}
}
