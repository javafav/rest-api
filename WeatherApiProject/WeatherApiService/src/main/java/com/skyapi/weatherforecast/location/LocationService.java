package com.skyapi.weatherforecast.location;

import java.util.List;

import org.springframework.stereotype.Service;

import com.skyapi.weatherforecast.AbstractLocationService;
import com.skyapi.weatherforecast.common.Location;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class LocationService extends AbstractLocationService {



	public LocationService(LocationRepository repo) {
		super();
		this.repo = repo;
	}
	
	public Location add(Location location) {
		return repo.save(location);
	}
	
	public List<Location> list(){
		return repo.findUntrashed(); 
	}
	
	
	public Location update(Location locationInRequest) {
		String code = locationInRequest.getCode();
		Location locationInDb = repo.findByCode(code);
		
		if(locationInDb == null) {
			throw new LocationNotFoundException(code);
		}
	

		locationInDb.copyFieldsFrom(locationInRequest);
		
		Location updatedLocation = repo.save(locationInDb);
		return updatedLocation;
	}
	
	public void delete(String code) {

		Location locationInDb = repo.findByCode(code);

		if (locationInDb == null) {
			throw new LocationNotFoundException(code);
		}

		repo.trashedByCode(code);
	}
}
