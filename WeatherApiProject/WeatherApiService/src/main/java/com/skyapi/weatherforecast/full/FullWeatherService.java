package com.skyapi.weatherforecast.full;

import org.springframework.stereotype.Service;

import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.location.LocationNotFoundException;
import com.skyapi.weatherforecast.location.LocationRepository;

@Service
public class FullWeatherService {

	private LocationRepository locationRepo;

	public FullWeatherService(LocationRepository locationRepo) {
		super();
		this.locationRepo = locationRepo;
	}
	
	
	public Location getByLocation(Location locationFromIP) {
		String cityName = locationFromIP.getCityName();
		String countryCode = locationFromIP.getCountryCode();
		
		Location locationInDB = locationRepo.findByCountryCodeAndCityName(countryCode, cityName);
		
		if(locationInDB == null) {
			throw new LocationNotFoundException(countryCode, cityName);
		}
		
		
		return locationInDB;
	}
	
	public Location getByLocationCode(String code) {
		Location locationInDB = locationRepo.findByCode(code);
		
		if(locationInDB == null) {
			throw new LocationNotFoundException(code);
		}
		return locationInDB;
	}
}
