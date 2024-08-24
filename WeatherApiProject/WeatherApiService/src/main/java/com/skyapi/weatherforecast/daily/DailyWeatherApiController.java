package com.skyapi.weatherforecast.daily;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.skyapi.weatherforecast.CommonUtlity;
import com.skyapi.weatherforecast.GeolocationService;
import com.skyapi.weatherforecast.common.DailyWeather;
import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.hourly.BadRequestException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/v1/daily")
@Validated
public class DailyWeatherApiController {

	private DailyWeatherService dailyWeatherService;
	private GeolocationService geolocationService;
	private ModelMapper modelMapper;

	public DailyWeatherApiController(DailyWeatherService dailyWeatherService, GeolocationService geolocationService,
			ModelMapper modelMapper) {
		super();
		this.dailyWeatherService = dailyWeatherService;
		this.geolocationService = geolocationService;
		this.modelMapper = modelMapper;
	}

	@GetMapping
	public ResponseEntity<?> listDailyForecastByIPAdress(HttpServletRequest request) {
		String ipAddress = CommonUtlity.getIPAddress(request);

		Location location = geolocationService.getLocation(ipAddress);
		System.out.println("ipAddress: " + ipAddress);
		List<DailyWeather> listDailyForecast = dailyWeatherService.getByLocation(location);

		if (listDailyForecast.isEmpty()) {
			return ResponseEntity.noContent().build();
		}

		return ResponseEntity.ok(listEntity2DTO(listDailyForecast));

	}

	@GetMapping("/{code}")
	public ResponseEntity<?> listDailyForecastByLocationCode(@PathVariable("code") String code) {
		List<DailyWeather> listDailyForecast = dailyWeatherService.getByLocationCode(code);

		if (listDailyForecast.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(listEntity2DTO(listDailyForecast));

	}
	
	@PutMapping("/{code}")
	public ResponseEntity<?> updateDailyForecast(@PathVariable("code") String code,  @RequestBody @Valid List<DailyWeatherDTO> listDTO) {
	
		if(listDTO.isEmpty()) {
			throw new BadRequestException("Daily forecast data cannot be empty");
		}
		
		List<DailyWeather> listDailyForecast = listDTO2ListEntity(listDTO);
	
		List<DailyWeather> updatedDailyForecast = dailyWeatherService.updateByLocationCode(code, listDailyForecast);


		return ResponseEntity.ok(listEntity2DTO(updatedDailyForecast));
	
	}

	private List<DailyWeather> listDTO2ListEntity(List<DailyWeatherDTO> listDTO) {
		List<DailyWeather> listForecast = new ArrayList<>();

		listDTO.forEach(dto -> {
			listForecast.add(modelMapper.map(dto, DailyWeather.class));

		});
		return listForecast;
	}

	private DailyWeatherListDTO listEntity2DTO(List<DailyWeather> dailyForecast) {
		Location location = dailyForecast.get(0).getId().getLocation();

		DailyWeatherListDTO listDTO = new DailyWeatherListDTO();
		listDTO.setLocation(location.toString());

		dailyForecast.forEach(forecast -> {

			listDTO.addDailyWeatherDTO(modelMapper.map(forecast, DailyWeatherDTO.class));

		});

		return listDTO;
	}

	private List<DailyWeather> listDTO2EntityList(List<DailyWeatherDTO> listDTO) {

		List<DailyWeather> listDailyWeather = new ArrayList<>();

		listDTO.forEach(dto -> {
			listDailyWeather.add(modelMapper.map(dto, DailyWeather.class));
		});

		return listDailyWeather;
	}
}
