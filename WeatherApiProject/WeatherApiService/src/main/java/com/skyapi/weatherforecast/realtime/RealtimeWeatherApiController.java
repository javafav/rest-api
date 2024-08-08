package com.skyapi.weatherforecast.realtime;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skyapi.weatherforecast.CommonUtlity;
import com.skyapi.weatherforecast.GeoLocationException;
import com.skyapi.weatherforecast.GeolocationService;
import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.common.RealtimeWeather;
import com.skyapi.weatherforecast.location.LocationNotFoundException;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/v1/realtime")
public class RealtimeWeatherApiController {

	private static final Logger LOGGER = LoggerFactory.getLogger(RealtimeWeatherApiController.class);
	private RealtimeWeatherService realtimeWeatherService;
	private GeolocationService geolocationService;
	private ModelMapper mapper;

  

	
	public RealtimeWeatherApiController(RealtimeWeatherService realtimeWeatherService,
			GeolocationService geolocationService, ModelMapper mapper) {
		super();
		this.realtimeWeatherService = realtimeWeatherService;
		this.geolocationService = geolocationService;
		this.mapper = mapper;
	}




	@GetMapping
	public ResponseEntity<?> getRealtimeWeatherByIPAddress(HttpServletRequest request){
		String ipAddress = CommonUtlity.getIPAddress(request);
		
		try {
			Location locationFromIp = geolocationService.getLocation(ipAddress);
			RealtimeWeather realtimeWeather = realtimeWeatherService.getByLocation(locationFromIp);
			
			 RealtimeWeatherDTO realtimeWeatherDTO = mapper.map(realtimeWeather, RealtimeWeatherDTO.class);
			
			return ResponseEntity.ok(realtimeWeatherDTO);
			
		} catch (GeoLocationException ex) {
		   LOGGER.error(ex.getMessage(), ex);
		   
		   return ResponseEntity.badRequest().build();
			
		} catch (LocationNotFoundException ex) {
			LOGGER.error(ex.getMessage(), ex);
			
			return ResponseEntity.notFound().build();
			
		}
		
	}
}
