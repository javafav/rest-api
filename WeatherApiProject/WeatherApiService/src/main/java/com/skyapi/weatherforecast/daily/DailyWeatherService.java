package com.skyapi.weatherforecast.daily;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.skyapi.weatherforecast.common.DailyWeather;
import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.location.LocationNotFoundException;
import com.skyapi.weatherforecast.location.LocationRepository;

@Service
public class DailyWeatherService {

	private DailyWeatherRepository dailyrepo;
	private LocationRepository locationRepo;

	public DailyWeatherService(DailyWeatherRepository dailyrepo, LocationRepository locationRepo) {
		super();
		this.dailyrepo = dailyrepo;
		this.locationRepo = locationRepo;
	}

	public List<DailyWeather> getByLocation(Location location) {

		String countryCode = location.getCountryCode();
		String cityName = location.getCityName();

	
		Location locationInDB = locationRepo.findByCountryCodeAndCityName(countryCode, cityName);

		if (locationInDB == null) {
			throw new LocationNotFoundException(countryCode, cityName);
		}

		return dailyrepo.findByLocationCode(locationInDB.getCode());
	}
	
	public List<DailyWeather> getByLocationCode(String code){
		
		Location locationInDB = locationRepo.findByCode(code);

		if (locationInDB == null) {
			throw new LocationNotFoundException(code);
		}
		
		return dailyrepo.findByLocationCode(code);
	}
	
	public List<DailyWeather> updateByLocationCode(String code, List<DailyWeather> dailyWeatherInRequest ){
		
		Location locationInDB = locationRepo.findByCode(code);

		if (locationInDB == null) {
			throw new LocationNotFoundException(code);
		}
		
		for(DailyWeather dailyWeather : dailyWeatherInRequest ) {
			
			dailyWeather.getId().setLocation(locationInDB);
		}
		
		
		List<DailyWeather> dailyWeatherInDb = locationInDB.getListDailyWeather();
		
		List<DailyWeather> dailyWeatherToBeRemoved = new ArrayList<>(); 
		
		for(DailyWeather dailyWeather : dailyWeatherInDb ) {
		
			if(!dailyWeatherInRequest.contains(dailyWeather)) {
				dailyWeatherToBeRemoved.add(dailyWeather.getShallowCopy());
			}
		}
		
		for(DailyWeather forecast :  dailyWeatherToBeRemoved) {
			dailyWeatherInDb.remove(forecast);
		}
	
		return (List<DailyWeather>) dailyrepo.saveAll(dailyWeatherInRequest);
	}
}
