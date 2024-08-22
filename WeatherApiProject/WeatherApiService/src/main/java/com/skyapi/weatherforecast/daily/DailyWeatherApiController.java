package com.skyapi.weatherforecast.daily;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skyapi.weatherforecast.CommonUtlity;
import com.skyapi.weatherforecast.GeoLocationException;
import com.skyapi.weatherforecast.GeolocationService;
import com.skyapi.weatherforecast.common.DailyWeather;
import com.skyapi.weatherforecast.common.Location;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/v1/daily")
public class DailyWeatherApiController {

	private DailyWeatherService dailyWeatherService;
	private GeolocationService geolocationService;
	
	
	public DailyWeatherApiController(DailyWeatherService dailyWeatherService, GeolocationService geolocationService) {
		super();
		this.dailyWeatherService = dailyWeatherService;
		this.geolocationService = geolocationService;
	}

	@GetMapping
	public ResponseEntity<?> listDailyForecastByIPAdress(HttpServletRequest request){
		String ipAddress = CommonUtlity.getIPAddress(request);
		
		try {
			
			Location location = geolocationService.getLocation(ipAddress);
			System.out.println("ipAddress: " + ipAddress);
			List<DailyWeather> listDailyForecast = dailyWeatherService.getByLocation(location);
			
			return ResponseEntity.ok(listDailyForecast);
			
	
		} catch (GeoLocationException ex) {
			
			return ResponseEntity.badRequest().build();
			
		}
		
	}
	
	
	
	
}
