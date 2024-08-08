package com.skyapi.weatherforecast;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class WeatherApiServiceApplication {

	@Bean
	public ModelMapper getModelMapper() {
		 ModelMapper mapper = new ModelMapper();
		 
		 return mapper;
	}
	
	public static void main(String[] args) {
		SpringApplication.run(WeatherApiServiceApplication.class, args);
	}

}
