package com.skyapi.weatherforecast.realtime;

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
import com.skyapi.weatherforecast.GeoLocationException;
import com.skyapi.weatherforecast.GeolocationService;
import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.common.RealtimeWeather;
import com.skyapi.weatherforecast.location.LocationNotFoundException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/realtime")
public class RealtimeWeatherApiController {

	private static final Logger LOGGER = LoggerFactory.getLogger(RealtimeWeatherApiController.class);
	private RealtimeWeatherService realtimeWeatherService;
	private GeolocationService geolocationService;
	private ModelMapper modelMapper;

  

	
	public RealtimeWeatherApiController(RealtimeWeatherService realtimeWeatherService,
			GeolocationService geolocationService, ModelMapper mapper) {
		super();
		this.realtimeWeatherService = realtimeWeatherService;
		this.geolocationService = geolocationService;
		this.modelMapper = mapper;
	}




	@GetMapping
	public ResponseEntity<?> getRealtimeWeatherByIPAddress(HttpServletRequest request){
		String ipAddress = CommonUtlity.getIPAddress(request);
		
		try {
			Location locationFromIp = geolocationService.getLocation(ipAddress);
			RealtimeWeather realtimeWeather = realtimeWeatherService.getByLocation(locationFromIp);
			
			return  ResponseEntity.ok(entity2DTO(realtimeWeather));
			
		} catch (GeoLocationException ex) {
		   LOGGER.error(ex.getMessage(), ex);
		   
		   return ResponseEntity.badRequest().build();
			
		} catch (LocationNotFoundException ex) {
			LOGGER.error(ex.getMessage(), ex);
			
			return ResponseEntity.notFound().build();
			
		}
		
	}
	
	@GetMapping("/{locationCode}")
	public ResponseEntity<?> getRealtimeByLocationCode(@PathVariable("locationCode") String locationCode){
		try {
			RealtimeWeather realtimeWeather = realtimeWeatherService.getByLocationCode(locationCode);
			
			
			return  ResponseEntity.ok(entity2DTO(realtimeWeather));
			
		} catch (LocationNotFoundException ex) {
			LOGGER.error(ex.getMessage(), ex);
			
			return ResponseEntity.notFound().build();
		}
	}
	
	@PutMapping("/{locationCode}")
	public ResponseEntity<?> updateRealtimeByLocationCode(@PathVariable("locationCode") String locationCode, @RequestBody @Valid RealtimeWeather realtimeWeatherInRequest){
		realtimeWeatherInRequest.setLocationCode(locationCode);
		try {
			RealtimeWeather updatedRealtimeWeather = realtimeWeatherService.update(locationCode, realtimeWeatherInRequest);
			
			return ResponseEntity.ok(entity2DTO(updatedRealtimeWeather));
			
		
		} catch (LocationNotFoundException ex) {
	       
			LOGGER.error(ex.getMessage(), ex);
			
			return ResponseEntity.notFound().build();
			
		}
		
	
	}
	
	private RealtimeWeatherDTO entity2DTO(RealtimeWeather realtimeWeather) {
		return modelMapper.map(realtimeWeather, RealtimeWeatherDTO.class);
	}
}
