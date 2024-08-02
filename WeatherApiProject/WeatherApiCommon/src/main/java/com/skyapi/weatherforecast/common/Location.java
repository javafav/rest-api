package com.skyapi.weatherforecast.common;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "locations")
public class Location {

	@Id
	@Column(length = 12, nullable = false, unique = true)
	private String code;

	@Column(length = 128, nullable = false)
	private String cityName;

	@Column(length = 64)
	private String reigonName;

	@Column(length = 64, nullable = false)
	private String countryName;

	@Column(length = 2, nullable = false)
	private String counrtyCode;

	private boolean enabled;
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

	public String getReigonName() {
		return reigonName;
	}

	public void setReigonName(String reigonName) {
		this.reigonName = reigonName;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public String getCounrtyCode() {
		return counrtyCode;
	}

	public void setCounrtyCode(String counrtyCode) {
		this.counrtyCode = counrtyCode;
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

}
