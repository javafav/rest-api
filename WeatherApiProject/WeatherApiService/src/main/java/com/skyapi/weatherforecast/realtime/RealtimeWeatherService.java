package com.skyapi.weatherforecast.realtime;

import java.util.Date;

import org.springframework.stereotype.Service;

import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.common.RealtimeWeather;
import com.skyapi.weatherforecast.location.LocationNotFoundException;
import com.skyapi.weatherforecast.location.LocationRepository;

@Service
public class RealtimeWeatherService {

	private RealtimeWeatherRepository realtimeWeatherRepo;;
	private LocationRepository locationRepo;

	
	
	
	public RealtimeWeatherService(RealtimeWeatherRepository weatherRepo, LocationRepository locationRepo) {
		super();
		this.realtimeWeatherRepo = weatherRepo;
		this.locationRepo = locationRepo;
	}


	public RealtimeWeather getByLocation(Location location) throws LocationNotFoundException {
		String cityName = location.getCityName();
		String countryCode = location.getCountryCode();
		
		RealtimeWeather realtimeWeather = realtimeWeatherRepo.findByCityAndCountryCode(cityName, countryCode);
		
		if(realtimeWeather == null) {
			throw new LocationNotFoundException("Could not find weather information for given city name and country code ");
		}
		return realtimeWeather;
		
	}
	
	
	public RealtimeWeather getByLocationCode(String locationCode) throws LocationNotFoundException {
		RealtimeWeather realtimeWeather = realtimeWeatherRepo.findByLocationCode(locationCode);
		
		if(realtimeWeather == null ) {
			throw new LocationNotFoundException("Could not find weather information for given code " + locationCode);
		}
		return realtimeWeather;
	}
	
	public RealtimeWeather update(String locationCode, RealtimeWeather realtimeWeatherInRequest) throws LocationNotFoundException {
		Location location = locationRepo.findByCode(locationCode);
		if(location == null) {
			throw new LocationNotFoundException("Could not find weather information for given code " + locationCode);
		}
		
		
		realtimeWeatherInRequest.setLocation(location);
		realtimeWeatherInRequest.setLastUpdated(new Date());
		
		if(location.getRealtimeWeather() == null) {
			location.setRealtimeWeather(realtimeWeatherInRequest);
			Location updatedLocation = locationRepo.save(location);
			return updatedLocation.getRealtimeWeather();
		}
		
	
		
		return realtimeWeatherRepo.save(realtimeWeatherInRequest);
		
		
	}
}
