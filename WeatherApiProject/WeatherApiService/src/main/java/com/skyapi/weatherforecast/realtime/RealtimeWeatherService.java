package com.skyapi.weatherforecast.realtime;

import org.springframework.stereotype.Service;

import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.common.RealtimeWeather;
import com.skyapi.weatherforecast.location.LocationNotFoundException;

@Service
public class RealtimeWeatherService {

	private RealtimeWeatherRepository repo;

	
	public RealtimeWeatherService(RealtimeWeatherRepository realtimeWeatherRepo) {
		super();
		this.repo = realtimeWeatherRepo;
	}
	
	public RealtimeWeather getByLocation(Location location) throws LocationNotFoundException {
		String cityName = location.getCityName();
		String countryCode = location.getCountryCode();
		
		RealtimeWeather realtimeWeather = repo.findByCityAndCountryCode(cityName, countryCode);
		
		if(realtimeWeather == null) {
			throw new LocationNotFoundException("Could not find weather information for given city name and country code ");
		}
		return realtimeWeather;
		
	}
}
