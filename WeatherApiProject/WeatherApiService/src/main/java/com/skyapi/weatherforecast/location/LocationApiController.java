package com.skyapi.weatherforecast.location;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.hourly.HourlyWeatherApiController;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/locations")
public class LocationApiController {

	private static final Logger LOGGER = LoggerFactory.getLogger(HourlyWeatherApiController.class);

	private LocationService service;
	private ModelMapper modelMapper;

	public LocationApiController(LocationService service, ModelMapper modelMapper) {
		super();
		this.service = service;
		this.modelMapper = modelMapper;
	}

	@PostMapping
	public ResponseEntity<LocationDTO> add(@RequestBody @Valid LocationDTO dto) {

		Location addedLocation = service.add(dto2Entity(dto));
		URI uri = URI.create("/v1/locations/" + addedLocation.getCode());

		return ResponseEntity.created(uri).body(entiy2DTO(addedLocation));
	}

	@GetMapping
	public ResponseEntity<?> listLocations() {
		List<Location> locations = service.list();

		if (locations.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(listEntity2ListDTO(locations));
	}

	@GetMapping("/{code}")
	public ResponseEntity<?> getLocation(@PathVariable("code") String code) {
		Location location = service.get(code);

		return ResponseEntity.ok(entiy2DTO(location));
	}

	@PutMapping
	public ResponseEntity<?> updateLocation(@RequestBody @Valid LocationDTO dto) {

		Location updatedLocation = service.update(dto2Entity(dto));
		return ResponseEntity.ok(entiy2DTO(updatedLocation));

	}

	@DeleteMapping("/{code}")
	public ResponseEntity<?> deleteLocation(@PathVariable("code") String code) {

		service.delete(code);
		return ResponseEntity.noContent().build();

	}

	private List<LocationDTO> listEntity2ListDTO(List<Location> locations) {
  
		List<LocationDTO> lstDTO = new ArrayList<>();
		
		locations.forEach(location -> {
			lstDTO.add(modelMapper.map(location, LocationDTO.class));
		});
		
		return lstDTO;
		
	}
	
	public LocationDTO entiy2DTO(Location location) {
		return modelMapper.map(location, LocationDTO.class );
	}

	public Location dto2Entity(LocationDTO dto) {
		return modelMapper.map(dto, Location.class );
	}
}
