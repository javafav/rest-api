package com.skyapi.weatherforecast;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.skyapi.weatherforecast.common.DailyWeather;
import com.skyapi.weatherforecast.common.HourlyWeather;
import com.skyapi.weatherforecast.common.Location;
import com.skyapi.weatherforecast.common.RealtimeWeather;
import com.skyapi.weatherforecast.daily.DailyWeatherDTO;
import com.skyapi.weatherforecast.full.FullWeatherDTO;
import com.skyapi.weatherforecast.hourly.HourlyWeatherDTO;
import com.skyapi.weatherforecast.realtime.RealtimeWeatherDTO;

@SpringBootApplication
public class WeatherApiServiceApplication {

	@Bean
	public ModelMapper getModelMapper() {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		configureHourlyWeather(mapper);

		configureDailyWeather(mapper);

		configureFullWeather(mapper);

		configureRealtimeWeather(mapper);

		return mapper;
	}
	
	@Bean
	public ObjectMapper getObjectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
		objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
		
		return objectMapper;
		
	}

	private void configureRealtimeWeather(ModelMapper mapper) {
		mapper.typeMap(RealtimeWeatherDTO.class, RealtimeWeather.class)
		.addMappings(m -> m.skip(RealtimeWeather::setLocation));
	}

	private void configureFullWeather(ModelMapper mapper) {
	   mapper.typeMap(Location.class, FullWeatherDTO.class)
		      .addMapping(src -> src.toString(), FullWeatherDTO::setLocation);
	}

	private void configureDailyWeather(ModelMapper mapper) {

		mapper.typeMap(DailyWeather.class, DailyWeatherDTO.class)

				.addMapping(src -> src.getId().getDayOfMonth(), DailyWeatherDTO::setDayOfMonth)
				.addMapping(src -> src.getId().getMonth(), DailyWeatherDTO::setMonth);

		mapper.typeMap(DailyWeatherDTO.class, DailyWeather.class)
				.addMapping(src -> src.getDayOfMonth(),
						(dest, value) -> dest.getId().setDayOfMonth(value != null ? (int) value : 0))
				.addMapping(src -> src.getMonth(),
						(dest, value) -> dest.getId().setMonth(value != null ? (int) value : 0));
	}

	private void configureHourlyWeather(ModelMapper mapper) {
		mapper.typeMap(HourlyWeather.class, HourlyWeatherDTO.class).addMapping(src -> src.getId().getHourOfDay(),
				HourlyWeatherDTO::setHourOfDay);

		mapper.typeMap(HourlyWeatherDTO.class, HourlyWeather.class).addMapping(src -> src.getHourOfDay(),
				(dest, value) -> dest.getId().setHourOfDay(value != null ? (int) value : 0));
	}

	public static void main(String[] args) {
		SpringApplication.run(WeatherApiServiceApplication.class, args);
	}

}
