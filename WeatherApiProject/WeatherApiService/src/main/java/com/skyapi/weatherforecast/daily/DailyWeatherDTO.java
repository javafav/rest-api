package com.skyapi.weatherforecast.daily;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DailyWeatherDTO {

	@JsonProperty("day_of_month")
	private int dayOfMonth;

	private int month;
	
	

	@JsonProperty("min_temp")
	private int minTemp;
	
	@JsonProperty("max_temp")
	private int maxTemp;
	
   
	private int precipitation;

	private String status;

	
	public int getDayOfMonth() {
		return dayOfMonth;
	}

	public void setDayOfMonth(int dayOfMonth) {
		this.dayOfMonth = dayOfMonth;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}



	public int getMinTemp() {
		return minTemp;
	}

	public void setMinTemp(int minTemp) {
		this.minTemp = minTemp;
	}

	public int getMaxTemp() {
		return maxTemp;
	}

	public void setMaxTemp(int maxTemp) {
		this.maxTemp = maxTemp;
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

}
