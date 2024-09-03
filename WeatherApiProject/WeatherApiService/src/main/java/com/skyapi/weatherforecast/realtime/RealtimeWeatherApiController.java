package com.skyapi.weatherforecast.realtime;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
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
import com.skyapi.weatherforecast.daily.DailyWeatherApiController;
import com.skyapi.weatherforecast.full.FullWeatherApiController;
import com.skyapi.weatherforecast.hourly.HourlyWeatherApiController;
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
	public ResponseEntity<?> getRealtimeWeatherByIPAddress(HttpServletRequest request) {
		String ipAddress = CommonUtlity.getIPAddress(request);

			Location locationFromIp = geolocationService.getLocation(ipAddress);
			RealtimeWeather realtimeWeather = realtimeWeatherService.getByLocation(locationFromIp);
			RealtimeWeatherDTO dto = entity2DTO(realtimeWeather);

			return ResponseEntity.ok(addLinksByIpAddress(dto));

		

	}

	@GetMapping("/{locationCode}")
	public ResponseEntity<?> getRealtimeByLocationCode(@PathVariable("locationCode") String locationCode) {

		RealtimeWeather realtimeWeather = realtimeWeatherService.getByLocationCode(locationCode);
		RealtimeWeatherDTO dto = entity2DTO(realtimeWeather);
		return ResponseEntity.ok(addLinksByLocation(dto, locationCode));

	}

	@PutMapping("/{locationCode}")
	public ResponseEntity<?> updateRealtimeByLocationCode(@PathVariable("locationCode") String locationCode,
			@RequestBody @Valid RealtimeWeatherDTO dto) {

		RealtimeWeather realtimeWeather = dto2Entity(dto);
		realtimeWeather.setLocationCode(locationCode);

		RealtimeWeather updatedRealtimeWeather = realtimeWeatherService.update(locationCode, realtimeWeather);
		RealtimeWeatherDTO updatedDto = entity2DTO(updatedRealtimeWeather);
		return ResponseEntity.ok(addLinksByLocation(updatedDto, locationCode));

	}

	private RealtimeWeatherDTO entity2DTO(RealtimeWeather realtimeWeather) {
		return modelMapper.map(realtimeWeather, RealtimeWeatherDTO.class);
	}

	private RealtimeWeather dto2Entity(RealtimeWeatherDTO dto) {
		return modelMapper.map(dto, RealtimeWeather.class);
	}
	
	private RealtimeWeatherDTO addLinksByIpAddress(RealtimeWeatherDTO dto) {
		
		dto.add(linkTo(
				   methodOn(RealtimeWeatherApiController.class).getRealtimeWeatherByIPAddress(null))
				   .withSelfRel());
		
		
		dto.add(linkTo(
				   methodOn(HourlyWeatherApiController.class).listHourlyForecastByIPAddress(null))
				   .withRel("hourly_forecast"));
		
		
		dto.add(linkTo(
				   methodOn(DailyWeatherApiController.class).listDailyForecastByIPAdress(null))
				   .withRel("daily_forecast"));
		

		dto.add(linkTo(
			            methodOn(FullWeatherApiController.class).getFullWeatherBYIPAddress(null))
				     .withRel("full_forecast"));
		
	
		
		
		
		return dto;
	}
	
	
	private RealtimeWeatherDTO addLinksByLocation(RealtimeWeatherDTO dto, String locationCode) {
		
		dto.add(linkTo(
				   methodOn(RealtimeWeatherApiController.class).getRealtimeByLocationCode(locationCode))
				   .withSelfRel());
		
		
		dto.add(linkTo(
				   methodOn(HourlyWeatherApiController.class).listHourlyWeatherForecastByLocationCode(null, locationCode))
				   .withRel("hourly_forecast"));
		
		
		dto.add(linkTo(
				   methodOn(DailyWeatherApiController.class).listDailyForecastByLocationCode(locationCode))
				   .withRel("daily_forecast"));
		

		dto.add(linkTo(
			            methodOn(FullWeatherApiController.class).getFullWeatherByLocationCode(locationCode))
				     .withRel("full_forecast"));
		
	
		
		
		
		return dto;
	}
}
