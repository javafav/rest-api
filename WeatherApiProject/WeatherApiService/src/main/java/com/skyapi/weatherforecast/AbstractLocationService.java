package com.skyapi.weatherforecast;

import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.location.LocationNotFoundException;
import com.skyapi.weatherforecast.location.LocationRepository;

public abstract class AbstractLocationService {

	protected LocationRepository repo;
	
	
	public Location get(String code){
		Location locationInDb = repo.findByCode(code);
		if (locationInDb == null) {
			throw new LocationNotFoundException(code);
		}
	
		return locationInDb;
	}
}
