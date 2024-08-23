package com.skyapi.weatherforecast.hourly;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import com.skyapi.weatherforecast.common.HourlyWeather;
import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.location.LocationNotFoundException;
import com.skyapi.weatherforecast.location.LocationRepository;

@Service
public class HourlyWeatherService {

	private HourlyWeatherRepository hourlyWeatherRepo;
	private LocationRepository locationRepo;
	
	
	public HourlyWeatherService(HourlyWeatherRepository hourlyWeatherRepo,
			                    LocationRepository locationRepo) {
		super();
		this.hourlyWeatherRepo = hourlyWeatherRepo;
		this.locationRepo = locationRepo;
	}

	public List<HourlyWeather> getByLocation(Location location, int currentHour){

		String countryCode = location.getCountryCode();
		String cityName = location.getCityName();

		Location locationInDB = locationRepo.findByCountryCodeAndCityName(countryCode, cityName);
    
		if (locationInDB == null) {

			throw new LocationNotFoundException("No location found with given country code and city name");
		}
         
		  return hourlyWeatherRepo.findByLocationCode(locationInDB.getCode(), currentHour);
	}
	
	public List<HourlyWeather> getByLocationCode(String locationCode, int currentHour)
		 {

		Location locationInDB = locationRepo.findByCode(locationCode);

		if (locationInDB == null) {

			throw new LocationNotFoundException(locationCode);
		}
		return hourlyWeatherRepo.findByLocationCode(locationCode, currentHour);

	}
	
	public List<HourlyWeather> updateByLocationCode(String locationCode, List<HourlyWeather> hourlyWeatherInRequest){
		Location locationInDB = locationRepo.findByCode(locationCode);

		if (locationInDB == null) {

			throw new LocationNotFoundException("No location found with given  location Code " + locationCode);
		}
		
	
		
		for(HourlyWeather item : hourlyWeatherInRequest) {
			item.getId().setLocation(locationInDB);
		}
		
		
		List<HourlyWeather> hourlyWeatherInDB = locationInDB.getListHourlyWeathers();
		List<HourlyWeather> hourlyWeathersToRemoved = new ArrayList<>();
		
		for(HourlyWeather item : hourlyWeatherInDB) {
			if(!hourlyWeatherInRequest.contains(item)) {
				hourlyWeathersToRemoved.add(item.getShallowCopy());
			}
		}
		
		for(HourlyWeather item : hourlyWeathersToRemoved) {
			hourlyWeatherInDB.remove(item);
		}
		
		
		return (List<HourlyWeather>) hourlyWeatherRepo.saveAll(hourlyWeatherInRequest);
	}
}
