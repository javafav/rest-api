package com.skyapi.weatherforecast.location;

import static org.assertj.core.api.Assertions.assertThat;

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
}
