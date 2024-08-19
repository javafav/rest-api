package com.skyapi.weatherforecast.location;

public class LocationNotFoundException extends RuntimeException {

	public LocationNotFoundException(String code) {
		super("No location found with given code " + code);
	}

	
	public LocationNotFoundException(String countryCode, String cityName) {
		super("Could not find weather information for given country code: "  + countryCode +  " and city name:  " + cityName);
	}
}
