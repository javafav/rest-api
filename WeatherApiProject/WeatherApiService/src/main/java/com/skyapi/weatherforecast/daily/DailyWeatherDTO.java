package com.skyapi.weatherforecast.daily;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"day_of_month", "month", "min_temp", "max_temp", "precipitation", "status"})
public class DailyWeatherDTO {

	@JsonProperty("day_of_month")
	@Range(min = 1, max = 31, message = "Day of month must be between 1-31")
	private int dayOfMonth;

	@Range(min = 1, max = 12, message = "Month must be between 1-12")
	private int month;
	
	

	@JsonProperty("min_temp")
	@Range(min = -50, max = 50, message = "Minimum temperature must be in the range of -50 to 50 Celsius degree")
	private int minTemp;
	
	@JsonProperty("max_temp")
	@Range(min = -50, max = 50, message = "Maximum temperature must be in the range of -50 to 50 Celsius degree")
	private int maxTemp;
	
	@Range(min = 0, max = 100, message = "Precipitation must be in the range of 0 to 100 percentage")
	private int precipitation;

	@Length(min = 3, max = 50, message = "Status must be in between 3-50 characters")
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
