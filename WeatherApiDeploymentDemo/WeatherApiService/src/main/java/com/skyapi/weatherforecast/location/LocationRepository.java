package com.skyapi.weatherforecast.location;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.skyapi.weatherforecast.common.Location;

public interface LocationRepository extends CrudRepository<Location, String> {

	@Query("SELECT l from Location l WHERE l.trashed = false")
	public List<Location> findUntrashed();
	
	
	@Query("SELECT l FROM Location l WHERE l.trashed = false AND l.code = ?1")
	public Location findByCode(String code);
	
	@Modifying
	@Query("UPDATE Location l SET l.trashed = true WHERE l.code = ?1")
	public void trashedByCode(String code);
	
	@Query("SELECT l FROM Location l WHERE  l.trashed = false AND l.countryCode = ?1 AND l.cityName = ?2 ")
	public Location findByCountryCodeAndCityName(String countryCode, String cityName);
}
