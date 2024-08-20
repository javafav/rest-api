package rest.client.examples.realtime.get;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import rest.client.examples.realtime.RealtimeWeather;

public class GetRealtimeWeatherByIPAsObject {

	public static void main(String[] args) {
		String requestURI = "http://localhost:8080/v1/realtime";
		
		RestTemplate restTemplate = new RestTemplate();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		String clientIpAddress = "108.30.178.78";	// New York city, USA
		headers.add("X-FORWARDED-FOR", clientIpAddress);
		
		HttpEntity<?> request = new HttpEntity<>(headers);
		
		var response = restTemplate.exchange(requestURI, HttpMethod.GET, request, RealtimeWeather.class);
		
		if (response.getStatusCode().is2xxSuccessful()) {
			RealtimeWeather body = response.getBody();
			System.out.println(body);
		}
	}

}
