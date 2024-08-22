package com.skyapi.weatherforecast.location;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.skyapi.weatherforecast.common.DailyWeather;
import com.skyapi.weatherforecast.common.HourlyWeather;
import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.common.RealtimeWeather;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class LocationRepositoryTests {

	private static final String END_URI_PATH = "/v1/locations";
	
	@Autowired
	private LocationRepository repo;
	
	
	
	@Test
	public void testAddNewLocation() {
		
		Location location = new Location();
		location.setCode("UCH");
		location.setCityName("Uch Sharif");
		location.setRegionName("Bahwalpur");
		location.setCountryCode("PK");
		location.setCountryName("Pakistan");
		
		Location savedLoaction = repo.save(location);
		
		assertThat(savedLoaction.getCode().equals("UCH"));
		
	}
	
	@Test
	public void testListSuccess() {
		List<Location> listLocations = repo.findUntrashed();
		
		assertThat(listLocations).isNotNull();
		listLocations.forEach(System.out::print);
	}
	
	@Test
	public void testLocationNotFoundByCode() {
		
		String code = "ABCD";
		Location location = repo.findByCode(code);
		
		assertThat(location).isNull();
		
	}
	
	@Test
	public void testLocationFoundByCode() {
		
		String code = "UCH_PK";
		Location location = repo.findByCode(code);
		
	
		System.out.println(location);
		
		assertThat(location).isNotNull();
		assertThat(location.getCode().equals(code));
		
	}
	
	@Test
	public void testTrashedSuccess() {
		String code = "UCH_PK";
		
		
		repo.trashedByCode(code);
		Location location = repo.findByCode(code);
		
		assertThat(location).isNull();
		
		
	}
	
	@Test
	public void testAddRealtimeWeather() {
	
		String code = "LACA_USA";
		Location location = repo.findByCode(code);
		
		RealtimeWeather realtimeWeather = location.getRealtimeWeather();
		
		if(realtimeWeather == null ) {
			realtimeWeather = new RealtimeWeather();
			realtimeWeather.setLocation(location);
			location.setRealtimeWeather(realtimeWeather);
		}
		
		
		realtimeWeather.setHumidity(30);
		realtimeWeather.setTemperature(39);
		realtimeWeather.setPrecipitation(36);
		realtimeWeather.setStatus("Cloudy");
	}
	
	

	@Test
	public void testAddHourlyWeather2Location() {
		String code = "UCH_PK";
		Location location = repo.findByCode(code);
		
		List<HourlyWeather> listHourlyWeathers = location.getListHourlyWeathers();
		HourlyWeather forecast1 = new HourlyWeather().id(8, location)
				                                 .precipitation(50)
				                                 .temperature(32)
				                                 .status("Sunny");
		
		HourlyWeather forecast2 = new HourlyWeather().id(9, location)
                .precipitation(48)
                .temperature(30)
                .status("Sunny");
		
		listHourlyWeathers.add(forecast1);
		listHourlyWeathers.add(forecast2);
		Location updatedLocation = repo.save(location);
		
		assertThat(updatedLocation.getListHourlyWeathers()).isNotEmpty();		
		
               
				                                
		
	}
	
	@Test
	public void testFindByCountryCodeAndCityNameNotFound() {
		String countryCode = "UAE";
		String cityName = "Sharjah";
		
		Location location = repo.findByCountryCodeAndCityName(countryCode, cityName);
		
		assertThat(location).isNull();
		
	}
	
	@Test
	public void testFindByCountryCodeAndCityNameTrashed() {
		String countryCode = "USA";
		String cityName = "Los Angeles";
		
		Location location = repo.findByCountryCodeAndCityName(countryCode, cityName);
		
		assertThat(location).isNull();
		
	}
	
	
	@Test
	public void testFindByCountryCodeAndCityNameFound() {
		String countryCode = "PAK";
		String cityName = "Lahore";
		
		Location location = repo.findByCountryCodeAndCityName(countryCode, cityName);
		
		assertThat(location).isNotNull();
		assertThat(location.getCountryCode()).isEqualTo(countryCode);
		assertThat(location.getCityName()).isEqualTo(cityName);
		
	}
	
	@Test
	public void testAddDailyForecast2Location() {
		
		String code = "UCH_PK";
		Location location = repo.findByCode(code);
		
		List<DailyWeather> listDailyWeather = location.getListDailyWeather();
		
		DailyWeather forecast1 = new DailyWeather()
				                     .precipitation(10).dayOfMonth(11)
				                     .month(11).location(location)
				                     .maxTemp(11).minTemp(7)
				                     .status("Cloudy");
		
		
		DailyWeather forecast2 = new DailyWeather()
				                     .precipitation(11).dayOfMonth(10)
				                     .month(10).location(location)
				                     .maxTemp(21).minTemp(17)
				                     .status("Sunny");
		  
		  
		
	  listDailyWeather.add(forecast1);
	  listDailyWeather.add(forecast2);
	  
	  Location updatedLocation = repo.save(location);
		
		 
		  
		  
		  assertThat(updatedLocation.getListDailyWeather().size()).isGreaterThan(0);
	 
		
	}
}
