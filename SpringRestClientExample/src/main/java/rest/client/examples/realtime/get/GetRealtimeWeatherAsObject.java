package rest.client.examples.realtime.get;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import rest.client.examples.realtime.RealtimeWeather;



public class GetRealtimeWeatherAsObject {

	public static void main(String[] args) {
		String requestURI = "http://localhost:8080/v1/realtime/{code}";
		RestTemplate restTemplate = new RestTemplate();

       Map<String, String> params = new HashMap<>();
		params.put("code", "LACA_USA");
		
	
		ResponseEntity<RealtimeWeather> response = restTemplate.getForEntity(requestURI, RealtimeWeather.class, params);
		
		HttpStatusCode statusCode = response.getStatusCode();
		System.out.println("Http status code: " + statusCode);
		
		if(statusCode.value() == HttpStatus.OK.value()) {
			RealtimeWeather weather = response.getBody();
			System.out.println(weather);
		}
		
		
	}
}
