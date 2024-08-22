package com.skyapi.weatherforecast.daily;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.skyapi.weatherforecast.common.DailyWeather;
import com.skyapi.weatherforecast.common.DailyWeatherId;
import com.skyapi.weatherforecast.common.Location;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class DailyWeatherRepositoryTests {
	
	@Autowired private DailyWeatherRepository repo;
	
	@Test
	public void testAddDailyWeather() {
		
		Location location = new Location().code("APE_PK");
		
		DailyWeather forecast = new DailyWeather()
                .precipitation(11).dayOfMonth(10)
                .month(10).location(location)
                .maxTemp(21).minTemp(17)
                .status("Sunny");
		
		DailyWeather savedForecast = repo.save(forecast);
		
		assertThat(savedForecast).isNotNull();
	}
	
	@Test
	public void testDeleteDailyWeather  () {
		Location location = new Location().code("APE_PK");
		
		DailyWeatherId id = new DailyWeatherId(10, 10, location);
		repo.deleteById(id);
		
		Optional<DailyWeather> forecast = repo.findById(id);
		assertThat(forecast).isEmpty();
	}
	
	@Test
	public void testFindByLocationCodeFound() {
		String code = "UCH_PK";
	
		
		List<DailyWeather> listDailyForecast = repo.findByLocationCode(code);
		
		assertThat(listDailyForecast.size()).isGreaterThan(0);
		
		
		listDailyForecast.forEach(System.out::println);
		
	}
	
	@Test
	public void testFindByLocationCodeNotFound() {
		String code = "ABC_PK";
	
		
		List<DailyWeather> listDailyForecast = repo.findByLocationCode(code);
		
		assertThat(listDailyForecast.size()).isZero();
		
		
		listDailyForecast.forEach(System.out::println);
		
	}
	
	

}
