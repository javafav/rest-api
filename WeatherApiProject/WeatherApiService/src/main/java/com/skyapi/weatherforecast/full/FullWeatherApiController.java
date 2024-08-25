package com.skyapi.weatherforecast.full;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skyapi.weatherforecast.CommonUtlity;
import com.skyapi.weatherforecast.GeolocationService;
import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.realtime.RealtimeWeatherDTO;

import jakarta.servlet.http.HttpServletRequest;

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
    	
    	Location locationInDB = weatherService.getByLocationCode(code);
    	return ResponseEntity.ok(entity2DTO(locationInDB));
    	
    }
    
    private FullWeatherDTO entity2DTO(Location entity) {
    	
   	 FullWeatherDTO dto = modelMapper.map(entity, FullWeatherDTO.class);
   	 dto.getRealtimeWeather().setLocation(null);
   	 return dto;
   }
}
