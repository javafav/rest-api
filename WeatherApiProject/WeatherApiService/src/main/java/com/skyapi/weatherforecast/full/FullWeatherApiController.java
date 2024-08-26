package com.skyapi.weatherforecast.full;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skyapi.weatherforecast.CommonUtlity;
import com.skyapi.weatherforecast.GeolocationService;
import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.hourly.BadRequestException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/full")
public class FullWeatherApiController {

	private static final Logger LOGGER = LoggerFactory.getLogger(FullWeatherApiController.class);
	
	private GeolocationService locationService;
    private ModelMapper modelMapper;
    private FullWeatherService weatherService;

    public FullWeatherApiController(GeolocationService locationService,
    		                         ModelMapper modelMapper,
			                         FullWeatherService weatherService) {
		super();
		this.locationService = locationService;
		this.modelMapper = modelMapper;
		this.weatherService = weatherService;
	}
    
    @GetMapping
    public ResponseEntity<?> getFullWeatherBYIPAddress(HttpServletRequest request){
    	String ipAddress = CommonUtlity.getIPAddress(request);
    	
    	Location locationFromIP = locationService.getLocation(ipAddress);
    	Location locationInDB = weatherService.getByLocation(locationFromIP);
    
    	return ResponseEntity.ok(entity2DTO(locationInDB));
    	
    }
    

    
    @GetMapping("/{code}")
    public ResponseEntity<?> getFullWeatherByLocationCode(@PathVariable("code") String code){
    	
    	Location locationInDB = weatherService.get(code);
    	return ResponseEntity.ok(entity2DTO(locationInDB));
    	
    }
    
	@PutMapping("/{locationCode}")
	public ResponseEntity<?> updateFullWeather(@PathVariable String locationCode, 
			@RequestBody @Valid FullWeatherDTO dto) throws BadRequestException {
		
		if (dto.getListHourlyWeather().isEmpty()) {
			throw new BadRequestException("Hourly weather data cannot be empty");
		}
		
		if (dto.getListDailyWeather().isEmpty()) {
			throw new BadRequestException("Daily weather data cannot be empty");
		}
		
		Location locationInRequest = dto2Entity(dto);
		
		Location updatedLocation = weatherService.update(locationCode, locationInRequest);
		
		return ResponseEntity.ok(entity2DTO(updatedLocation));
	}
	
    private FullWeatherDTO entity2DTO(Location entity) {
    	
   	 FullWeatherDTO dto = modelMapper.map(entity, FullWeatherDTO.class);
   	 dto.getRealtimeWeather().setLocation(null);
   	
   	 return dto;
   }
    
    private Location dto2Entity(FullWeatherDTO dto) {
    	 return modelMapper.map(dto, Location.class);
    }
}
