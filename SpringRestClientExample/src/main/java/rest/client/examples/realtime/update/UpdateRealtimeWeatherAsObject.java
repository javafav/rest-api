package rest.client.examples.realtime.update;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import rest.client.examples.realtime.RealtimeWeather;

public class UpdateRealtimeWeatherAsObject {

	public static void main(String[] args) {
		String requestURI = "http://localhost:8080/v1/realtime/{code}";
		String locationCode = "APE_PK";
		
		RestTemplate restTemplate = new RestTemplate();
		
		RealtimeWeather realtimeWeather = new RealtimeWeather();
		realtimeWeather.setTemperature(12);
		realtimeWeather.setHumidity(60);
		realtimeWeather.setPrecipitation(15);
		realtimeWeather.setWindSpeed(5);
		realtimeWeather.setStatus("Clear");
		
		var request = new HttpEntity<>(realtimeWeather);
		
		var response = restTemplate.exchange(
				requestURI, HttpMethod.PUT, request, RealtimeWeather.class, locationCode);
		
		if (response.getStatusCode().is2xxSuccessful()) {
			System.out.println("Realtime Weather updated");
			RealtimeWeather body = response.getBody();
			System.out.println(body);
		}

	}

}
