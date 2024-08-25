package rest.client.examples.realtime.update;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class UpdateRealtimeWeatherAsJSONString {

	public static void main(String[] args) {
		String requestURI = "http://localhost:8080/v1/realtime/{code}";
		String locationCode = "DELHI_IN";
		
		RestTemplate restTemplate = new RestTemplate();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		String json = """
					{
					  "temperature": 12,
					  "humidity": 55,
					  "precipitation": 23,
					  "status": "Cloudy",
					  "wind_speed": 8
					}				
				""";
		
		HttpEntity<String> request = new HttpEntity<>(json, headers);
		
		try {
			restTemplate.put(requestURI, request, locationCode);
			System.out.println("Realtime Weather updated");
		} catch (RestClientResponseException ex) {
			System.out.println("Error status code: " + ex.getStatusCode());
			ex.printStackTrace();
		}
	}

}
