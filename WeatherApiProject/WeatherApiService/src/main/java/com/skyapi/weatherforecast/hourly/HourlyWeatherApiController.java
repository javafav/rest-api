package com.skyapi.weatherforecast.hourly;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skyapi.weatherforecast.CommonUtlity;
import com.skyapi.weatherforecast.GeoLocationException;
import com.skyapi.weatherforecast.GeolocationService;
import com.skyapi.weatherforecast.common.HourlyWeather;
import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.location.LocationNotFoundException;
import com.skyapi.weatherforecast.realtime.RealtimeWeatherApiController;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/v1/hourly")
public class HourlyWeatherApiController {

	private static final Logger LOGGER = LoggerFactory.getLogger(HourlyWeatherApiController.class);
	private GeolocationService locationService;
	private HourlyWeatherService hourlyWeatherService;
	
	public HourlyWeatherApiController(GeolocationService locationService, HourlyWeatherService hourlyWeatherService) {
		super();
		this.locationService = locationService;
		this.hourlyWeatherService = hourlyWeatherService;
	}
	
	@GetMapping
	public ResponseEntity<?> listHourlyWeatherByIPAddress(HttpServletRequest request){
		String ipAddress = CommonUtlity.getIPAddress(request);
		int currentHour = Integer.parseInt(request.getHeader("X-FORWORD-HOUR"));
		
		try {
		
			Location ipLocation = locationService.getLocation(ipAddress);
		
		if(ipLocation == null) {
			return ResponseEntity.noContent().build();
		}
	
		List<HourlyWeather> listHourlyWeather = hourlyWeatherService.getByLocation(ipLocation, currentHour);
		return ResponseEntity.ok(listHourlyWeather);
		
		}catch (GeoLocationException ex) {
			LOGGER.error(ex.getMessage(), ex);
			
			return ResponseEntity.badRequest().build();
		} catch (LocationNotFoundException ex) {
			LOGGER.error(ex.getMessage(), ex);
			
			return ResponseEntity.notFound().build();
		}
		
	}
	
	
}
