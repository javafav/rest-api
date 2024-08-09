package com.skyapi.weatherforecast.realtime;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.skyapi.weatherforecast.common.RealtimeWeather;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class RealtimeWeatherRepositoryTests {

	@Autowired
	private RealtimeWeatherRepository repo;
	
	@Test
	public void testUpdate() {
		String locationCode = "LACA_USA";
		RealtimeWeather realtimeWeather = repo.findById(locationCode).get();
		
		realtimeWeather.setLastUpdated(new Date());
		realtimeWeather.setHumidity(30);
		realtimeWeather.setTemperature(19);
		realtimeWeather.setPrecipitation(16);
		realtimeWeather.setStatus("Snowy");
		
		RealtimeWeather updatedWeather = repo.save(realtimeWeather);
		
		assertThat(updatedWeather.getStatus().equals("Snowy"));
		
	}
	
	@Test
	public void testFindByCityNameAndCountryCodeNotFound() {
		
		String cityName = "Dubai";
		String CountryCode = "UAE";
		
		RealtimeWeather realtimeWeather = repo.findByCityAndCountryCode(cityName, CountryCode);
		
		assertThat(realtimeWeather).isNull();
	}
	
	@Test
	public void testFindByCityNameAndCountryCodeFound() {
		
		String cityName = "Los Angeles";
		String CountryCode = "USA";
		
		RealtimeWeather realtimeWeather = repo.findByCityAndCountryCode(cityName, CountryCode);
		
		assertThat(realtimeWeather).isNotNull();
		
		//assertThat(realtimeWeather.getLocation().getCityName()).isEqualTo("Uch Sharif");
	}
	
	@Test
	public void testFindByLocationNotFound() {
		String code = "ABC_TEST";
		RealtimeWeather realtimeWeather = repo.findByLocationCode(code);
		
		assertThat(realtimeWeather).isNull();
		
	}
	
	@Test
	public void testFindByTrashedLocationNotFound() {
		String code = "UCH_PK";
		RealtimeWeather realtimeWeather = repo.findByLocationCode(code);
		
		assertThat(realtimeWeather).isNull();
		
	}
	
	@Test
	public void testFindByLocationFound() {
		String code = "UCH_PK";
		RealtimeWeather realtimeWeather = repo.findByLocationCode(code);
		
		assertThat(realtimeWeather).isNotNull();
		assertThat(realtimeWeather.getLocation().getCode()).isEqualTo("UCH_PK");
		assertThat(realtimeWeather.getLocation().getCityName()).isEqualTo("Hasilpur");
		
		System.out.println(realtimeWeather);
		
	}
}
