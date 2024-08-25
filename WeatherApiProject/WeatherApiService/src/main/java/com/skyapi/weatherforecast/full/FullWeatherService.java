package com.skyapi.weatherforecast.full;

import java.util.List;

import org.springframework.stereotype.Service;

import com.skyapi.weatherforecast.common.DailyWeather;
import com.skyapi.weatherforecast.common.HourlyWeather;
import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.common.RealtimeWeather;
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
	
	public Location update(String code, Location locationInRequest) {
		Location locationInDB = locationRepo.findByCode(code);
		
		if(locationInDB == null) {
			throw new LocationNotFoundException(code);
		}
		
		RealtimeWeather realtimeWeather = locationInRequest.getRealtimeWeather();
		realtimeWeather.setLocation(locationInDB);
		
	   List<HourlyWeather> listHourlyWeather = locationInRequest.getListHourlyWeather();
	   listHourlyWeather.forEach(hw -> hw.getId().setLocation(locationInDB));
	   
	   List<DailyWeather> listDailyWeather = locationInRequest.getListDailyWeather();
	   listDailyWeather.forEach(dw -> dw.getId().setLocation(locationInDB));
	   
	   locationInDB.setCode(locationInRequest.getCode());
	   locationInDB.setCityName(locationInRequest.getCityName());
	   locationInDB.setRegionName(locationInRequest.getRegionName());
	   locationInDB.setCountryCode(locationInRequest.getCountryCode());
	   locationInDB.setCountryName(locationInRequest.getCountryName());
	   locationInDB.setEnabled(locationInRequest.isEnabled());
	   locationInDB.setTrashed(locationInRequest.isTrashed());
		
	   return locationRepo.save(locationInRequest);
		
	}
}
