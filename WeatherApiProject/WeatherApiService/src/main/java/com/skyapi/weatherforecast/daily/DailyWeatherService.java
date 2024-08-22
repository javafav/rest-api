package com.skyapi.weatherforecast.daily;

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

		System.out.println(location.getCode());
		Location locationInDB = locationRepo.findByCountryCodeAndCityName(countryCode, cityName);

		if (locationInDB == null) {
			throw new LocationNotFoundException(countryCode, cityName);
		}

		return dailyrepo.findByLocationCode(locationInDB.getCode());
	}
}
