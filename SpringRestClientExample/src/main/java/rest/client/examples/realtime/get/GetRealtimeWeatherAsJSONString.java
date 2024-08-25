package rest.client.examples.realtime.get;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class GetRealtimeWeatherAsJSONString {

	public static void main(String[] args) {
		String requestURI = "http://localhost:8080/v1/realtime/{code}";
		RestTemplate restTemplate = new RestTemplate();
		
		Map<String, String> params = new HashMap<>();
		params.put("code", "UCH_PK");
		
	
		ResponseEntity<String> response = restTemplate.getForEntity(requestURI, String.class, params);
		System.out.println(response);
	}
}
