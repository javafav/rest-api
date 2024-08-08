package com.skyapi.weatherforecast.realtime;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.skyapi.weatherforecast.common.RealtimeWeather;

public interface RealtimeWeatherRepository extends CrudRepository<RealtimeWeather, String> {

	@Query("SELECT r FROM RealtimeWeather r WHERE r.location.cityName = ?1 AND r.location.countryCode = ?2")
	public RealtimeWeather findByCityAndCountryCode(String cityName, String countryCode );
}
