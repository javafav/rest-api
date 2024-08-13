package com.skyapi.weatherforecast.hourly;

import java.util.ArrayList;
import java.util.List;

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
	private ModelMapper modelMapper;
	

	
	public HourlyWeatherApiController(GeolocationService locationService, HourlyWeatherService hourlyWeatherService,
			ModelMapper modelMapper) {
		super();
		this.locationService = locationService;
		this.hourlyWeatherService = hourlyWeatherService;
		this.modelMapper = modelMapper;
	}


	@GetMapping
	public ResponseEntity<?> listHourlyWeatherByIPAddress(HttpServletRequest request) {

		String ipAddress = CommonUtlity.getIPAddress(request);
		
		try {
			
			int currentHour = Integer.parseInt(request.getHeader("X-Current-Hour"));
             
			Location ipLocation = locationService.getLocation(ipAddress);
			
			String countryCode = ipLocation.getCountryCode();
			String cityName = ipLocation.getCityName();
		    System.out.println("Country Code Test: " + countryCode +  "City Name Test:"+ cityName );

			List<HourlyWeather> listHourlyWeather = hourlyWeatherService.getByLocation(ipLocation, currentHour);
           
			if (listHourlyWeather.isEmpty() ) {
				return ResponseEntity.noContent().build();
			}
			return ResponseEntity.ok(listEntity2DTO(listHourlyWeather));

		} catch (NumberFormatException | GeoLocationException ex) {
			LOGGER.error(ex.getMessage(), ex);

			return ResponseEntity.badRequest().build();
	
		} catch (LocationNotFoundException ex) {
			LOGGER.error(ex.getMessage(), ex);

			return ResponseEntity.notFound().build();
		}

	}
	
	
	private HourlyWeatherListDTO listEntity2DTO(List<HourlyWeather> hourlyFoecast){
        Location location = hourlyFoecast.get(0).getId().getLocation();
        
        HourlyWeatherListDTO listDTO = new HourlyWeatherListDTO();
         listDTO.setLocation(location.toString());
        
       hourlyFoecast.forEach(hourlyWeather -> {
    	   
        	HourlyWeatherDTO dto = modelMapper.map(hourlyWeather, HourlyWeatherDTO.class);
        	listDTO.add(dto);
        });
		
		return  listDTO;
	}
	
	
}
