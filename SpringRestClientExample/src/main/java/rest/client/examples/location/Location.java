package rest.client.examples.location;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Location {

	private String code;
	
	@JsonProperty("city_name")
	private String cityName;
	
	@JsonProperty("region_name")
	@JsonInclude(value = Include.NON_NULL)
	private String regionName;
	
	@JsonProperty("country_code")
	private String countryCode;
	
	@JsonProperty("country_name")
	private String countryName;
	
	private boolean enabled;

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

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getCountryName() {
		return countryName;
	}

	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public String toString() {
		return "Location [code=" + code + ", cityName=" + cityName + ", regionName=" + regionName + ", countryCode="
				+ countryCode + ", countryName=" + countryName + ", enabled=" + enabled + "]";
	}
	
	

}
