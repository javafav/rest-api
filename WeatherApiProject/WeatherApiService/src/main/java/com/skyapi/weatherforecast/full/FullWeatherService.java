package com.skyapi.weatherforecast.full;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.skyapi.weatherforecast.AbstractLocationService;
import com.skyapi.weatherforecast.common.DailyWeather;
import com.skyapi.weatherforecast.common.HourlyWeather;
import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.common.RealtimeWeather;
import com.skyapi.weatherforecast.location.LocationNotFoundException;
import com.skyapi.weatherforecast.location.LocationRepository;

@Service
public class FullWeatherService extends AbstractLocationService {

	private LocationRepository locationRepo;

	public FullWeatherService(LocationRepository locationRepo) {
		super();
		this.locationRepo = locationRepo;
	}

	public Location getByLocation(Location locationFromIP) {
		String cityName = locationFromIP.getCityName();
		String countryCode = locationFromIP.getCountryCode();

		Location locationInDB = locationRepo.findByCountryCodeAndCityName(countryCode, cityName);

		if (locationInDB == null) {
			throw new LocationNotFoundException(countryCode, cityName);
		}

		return locationInDB;
	}

	public Location update(String locationCode, Location locationInRequest) {
		Location locationInDB = locationRepo.findByCode(locationCode);

		if (locationInDB == null) {
			throw new LocationNotFoundException(locationCode);
		}

		RealtimeWeather realtimeWeather = locationInRequest.getRealtimeWeather();
		realtimeWeather.setLocation(locationInDB);
		realtimeWeather.setLastUpdated(new Date());

		setLocationForWeatherData(locationInRequest, locationInDB);

		setRealtimeIfNotExistBefore(locationInDB, locationInRequest);

		locationInRequest.copyAllFieldsFrom(locationInDB);

		return locationRepo.save(locationInRequest);
	}

	private void setRealtimeIfNotExistBefore(Location locationInDB, Location locationInRequest) {
		if (locationInDB.getRealtimeWeather() == null) {

			locationInDB.setRealtimeWeather(locationInRequest.getRealtimeWeather());
			locationRepo.save(locationInDB);
		}
	}

	private void setLocationForWeatherData(Location locationInRequest, Location locationInDB) {
		List<DailyWeather> listDailyWeather = locationInRequest.getListDailyWeather();
		listDailyWeather.forEach(dw -> dw.getId().setLocation(locationInDB));

		List<HourlyWeather> listHourlyWeather = locationInRequest.getListHourlyWeather();
		listHourlyWeather.forEach(hw -> hw.getId().setLocation(locationInDB));
	}
}
