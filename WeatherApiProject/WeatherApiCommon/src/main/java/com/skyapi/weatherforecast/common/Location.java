package com.skyapi.weatherforecast.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.hibernate.validator.constraints.Length;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "locations")
public class Location {

	@Id
	@Column(length = 12, nullable = false, unique = true)
	@NotNull(message = "Location code cannot be null")
	@Length(min = 3, max = 12, message = "Location code must have 3-12 charcter")
	private String code;

	@Column(length = 128, nullable = false)
	@JsonProperty("city_name")
	@NotNull(message = "City name cannot be null")
	@Length(min = 3, max = 12, message = "City name must have 3-128 charcter")
	private String cityName;

	@Column(length = 64)
	@JsonProperty("region_name")
	@Length(min = 3, max = 12, message = "Region name must have 3-64 charcter")
	private String regionName;

	@Column(length = 64, nullable = false)
	@JsonProperty("country_name")
	@NotNull(message = "Country name cannot be null")
	@Length(min = 3, max = 12, message = "Country name must have 3-64 charcter")
	private String countryName;

	@Column(length = 3, nullable = false)
	@JsonProperty("country_code")
	@NotNull(message = "Country code cannot be null")
	private String countryCode;

	private boolean enabled;
	
	@OneToOne(mappedBy = "location" , cascade = CascadeType.ALL)
	@PrimaryKeyJoinColumn
	@JsonIgnore
	private RealtimeWeather realtimeWeather;
	
	@OneToMany(mappedBy = "hourlyWeatherId.location", cascade = CascadeType.ALL)
	private List<HourlyWeather> listHourlyWeathers =  new ArrayList<>();
	
	public Location() {}
	
	public Location( String cityName, String regionName, String countryName,String countryCode) {
		
		this.cityName = cityName;
		this.regionName = regionName;
		this.countryName = countryName;
		this.countryCode = countryCode;
	}

	@JsonIgnore
	private boolean trashed;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
	
		this.code = code;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

   public String getRegionName() {
		return regionName;
	}

	public void setRegionName(String regionName) {
		this.regionName = regionName;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}
  

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public boolean isTrashed() {
		return trashed;
	}

	public void setTrashed(boolean trashed) {
		this.trashed = trashed;
	}

	@Override
	public int hashCode() {
		return Objects.hash(code);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Location other = (Location) obj;
		return Objects.equals(code, other.code);
	}

	@Override
	public String toString() {
		return cityName + ", " +  (regionName != null ? regionName + ", " : "" ) +    countryName;
	}

	public RealtimeWeather getRealtimeWeather() {
		return realtimeWeather;
	}

	public void setRealtimeWeather(RealtimeWeather realtimeWeather) {
		this.realtimeWeather = realtimeWeather;
	}

	public List<HourlyWeather> getListHourlyWeathers() {
		return listHourlyWeathers;
	}

	public void setListHourlyWeathers(List<HourlyWeather> listHourlyWeathers) {
		this.listHourlyWeathers = listHourlyWeathers;
	}
	
	public Location code(String code) {
		setCode(code);
		return this;
	}
	
	

}
