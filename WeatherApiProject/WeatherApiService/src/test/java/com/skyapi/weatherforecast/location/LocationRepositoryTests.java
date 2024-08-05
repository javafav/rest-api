package com.skyapi.weatherforecast.location;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.skyapi.weatherforecast.common.Location;

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
		location.setCounrtyCode("PK");
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
		
		
		repo.trashByCode(code);
		Location location = repo.findByCode(code);
		
		assertThat(location).isNull();
		
		
	}
}
