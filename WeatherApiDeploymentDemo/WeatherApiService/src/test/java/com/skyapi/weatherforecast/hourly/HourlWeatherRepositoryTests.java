package com.skyapi.weatherforecast.hourly;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.skyapi.weatherforecast.common.HourlyWeather;
import com.skyapi.weatherforecast.common.HourlyWeatherId;
import com.skyapi.weatherforecast.common.Location;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class HourlWeatherRepositoryTests {

	@Autowired
	private HourlyWeatherRepository repo;
	
	@Test
	public void testAddHourlyForecast() {
		Location location = new Location();
		
		location.setCode("LACA_USA");
		
		HourlyWeather forecast1 = new HourlyWeather().id(9, location)
                .precipitation(40)
                .temperature(32)
                .status("Sunny");
		
		HourlyWeather forecast2 = new HourlyWeather().location(location).hourOfDay(10)
                .precipitation(38)
                .temperature(30)
                .status("Cloudy");
		
		List<HourlyWeather> forecasts = Arrays.asList(forecast1, forecast2);
		
		repo.saveAll(forecasts);
		
	}
	
	
	@Test
	public void testDeleteHourlyForecast() {

		Location location = new Location().code("LACA_USA");

		HourlyWeatherId hourlyWeatherId = new HourlyWeatherId(10, location);

		repo.deleteById(hourlyWeatherId);

		Optional<HourlyWeather> findById = repo.findById(hourlyWeatherId);

		assertThat(findById).isEmpty();

	}
	
	@Test
	public void testFindByLocationCodeFound() {
		String locationCode = "UCH_PK";
		int currenrHour = 7;
		
		List<HourlyWeather> hourlyWeatherList = repo.findByLocationCode(locationCode, currenrHour);
		
		assertThat(hourlyWeatherList).isNotEmpty();
		
		hourlyWeatherList.forEach(hour -> System.out.println(hour));
	}
	
	@Test
	public void testFindByLocationCodeNotFound() {
		String locationCode = "UCH_PK";
		int currenrHour = 10;
		
		List<HourlyWeather> hourlyWeatherList = repo.findByLocationCode(locationCode, currenrHour);
		
		assertThat(hourlyWeatherList).isEmpty();
		
		hourlyWeatherList.forEach(hour -> System.out.println(hour));
	}
}
