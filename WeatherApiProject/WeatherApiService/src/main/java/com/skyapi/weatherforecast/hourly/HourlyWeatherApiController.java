package com.skyapi.weatherforecast.hourly;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skyapi.weatherforecast.CommonUtlity;
import com.skyapi.weatherforecast.GeoLocationException;
import com.skyapi.weatherforecast.GeolocationService;
import com.skyapi.weatherforecast.common.HourlyWeather;
import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.daily.DailyWeatherApiController;
import com.skyapi.weatherforecast.full.FullWeatherApiController;
import com.skyapi.weatherforecast.location.LocationNotFoundException;
import com.skyapi.weatherforecast.realtime.RealtimeWeatherApiController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/hourly")
@Validated
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
	public ResponseEntity<?> listHourlyForecastByIPAddress(HttpServletRequest request) {

		String ipAddress = CommonUtlity.getIPAddress(request);

		try {

			int currentHour = Integer.parseInt(request.getHeader("X-Current-Hour"));

			Location ipLocation = locationService.getLocation(ipAddress);


			List<HourlyWeather> listHourlyWeather = hourlyWeatherService.getByLocation(ipLocation, currentHour);

			if (listHourlyWeather.isEmpty()) {
				return ResponseEntity.noContent().build();
			}
			HourlyWeatherListDTO dto = listEntity2DTO(listHourlyWeather);
			return ResponseEntity.ok(addLinksByIpAddress(dto));

		} catch (NumberFormatException ex ) {
			LOGGER.error(ex.getMessage(), ex);

			return ResponseEntity.badRequest().build();

		} 
	}
	
	@GetMapping("/{locationCode}")
	public ResponseEntity<?> listHourlyWeatherForecastByLocationCode(HttpServletRequest request,
			@PathVariable("locationCode") String locationCode) {

		try {

			int currentHour = Integer.parseInt(request.getHeader("X-Current-Hour"));

			List<HourlyWeather> listHourlyWeather = hourlyWeatherService.getByLocationCode(locationCode, currentHour);

			if (listHourlyWeather.isEmpty()) {
				return ResponseEntity.noContent().build();
			}
			HourlyWeatherListDTO dto = listEntity2DTO(listHourlyWeather);
			return ResponseEntity.ok(addLinksByLocation(locationCode, dto));

		} catch (NumberFormatException ex) {
			LOGGER.error(ex.getMessage(), ex);

			return ResponseEntity.badRequest().build();

		} 

	}
	
	@PutMapping("/{locationCode}")
	public ResponseEntity<?> updateHourlyWeatherForecastByLocationCode(
			@PathVariable("locationCode") String locationCode, @RequestBody @Valid List<HourlyWeatherDTO> listDTO)
			throws BadRequestException {

		if (listDTO.isEmpty()) {
			throw new BadRequestException("Hourly forecast data cannot be empty");
		}

		List<HourlyWeather> hourlyWeather = listDTO2ListEntity(listDTO);
		listDTO.forEach(System.out::print);
		hourlyWeather.forEach(System.out::print);

		List<HourlyWeather> updatedHourlyForecast = hourlyWeatherService.updateByLocationCode(locationCode,
				hourlyWeather);

		HourlyWeatherListDTO dto = listEntity2DTO(updatedHourlyForecast);

		return ResponseEntity.ok(addLinksByLocation(locationCode, dto));

	}
	
	private List<HourlyWeather> listDTO2ListEntity(List<HourlyWeatherDTO> listDTO) {
		 List<HourlyWeather> listForecast = new ArrayList<>();
		 
		 listDTO.forEach(dto -> {
			 listForecast.add( modelMapper.map(dto, HourlyWeather.class));
			
		 });
		 return listForecast;
	}
	
	
	
	private HourlyWeatherListDTO listEntity2DTO(List<HourlyWeather> hourlyFoecast){
        Location location = hourlyFoecast.get(0).getId().getLocation();
        
        HourlyWeatherListDTO listDTO = new HourlyWeatherListDTO();
        listDTO.setLocation(location.toString());
        
       hourlyFoecast.forEach(hourlyWeather -> {
    	   
        listDTO.addHourlyFoecast(modelMapper.map(hourlyWeather, HourlyWeatherDTO.class));
        	
        });
		
		return  listDTO;
	}
	
	public HourlyWeatherListDTO addLinksByIpAddress(HourlyWeatherListDTO dto) {
		
		dto.add(linkTo(
				       methodOn(HourlyWeatherApiController.class).listHourlyForecastByIPAddress(null))
				       .withSelfRel());
		dto.add(linkTo(
				   methodOn(RealtimeWeatherApiController.class).getRealtimeWeatherByIPAddress(null))
				   .withRel("realtime") );
		
		dto.add(linkTo(
				   methodOn(DailyWeatherApiController.class).listDailyForecastByIPAdress(null))
				   .withRel("daily_forecast"));
		

		dto.add(linkTo(
			            methodOn(FullWeatherApiController.class).getFullWeatherBYIPAddress(null))
				     .withRel("full_forecast"));
		
		
		
		return dto;
	}
	
	public HourlyWeatherListDTO addLinksByLocation(String locationCode, HourlyWeatherListDTO dto) {
		
		dto.add(linkTo(
				       methodOn(HourlyWeatherApiController.class).listHourlyWeatherForecastByLocationCode(null, locationCode))
				       .withSelfRel());
		dto.add(linkTo(
				   methodOn(RealtimeWeatherApiController.class).getRealtimeByLocationCode(locationCode))
				   .withRel("realtime") );
		
		dto.add(linkTo(
				   methodOn(DailyWeatherApiController.class).listDailyForecastByLocationCode(locationCode))
				   .withRel("daily_forecast"));
		

		dto.add(linkTo(
			            methodOn(FullWeatherApiController.class).getFullWeatherByLocationCode(locationCode))
				     .withRel("full_forecast"));
		
		
		
		return dto;
	}
	
}
