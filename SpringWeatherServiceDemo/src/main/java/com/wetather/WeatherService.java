package com.wetather;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

@Service
public class WeatherService {

	@Autowired
	private RestTemplate restTemplate;

	@Value("${api.realtime.weather.uri}")
	private String getRealtimeWeatherRequestURI;

	public RealtimeWeather getForecasat() throws WeatherServiceException {
		try {
			return restTemplate.getForObject(getRealtimeWeatherRequestURI, RealtimeWeather.class);
		} catch (RestClientResponseException ex) {

			System.out.println(ex.getStatusCode());

			String message = "Error calling Get Realtime Weather API: " + ex.getMessage();
			throw new WeatherServiceException(message);
		}

	}

}
