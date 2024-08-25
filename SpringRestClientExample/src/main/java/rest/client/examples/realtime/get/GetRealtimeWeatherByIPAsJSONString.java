package rest.client.examples.realtime.get;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class GetRealtimeWeatherByIPAsJSONString {

	public static void main(String[] args) {
		String requestURI = "http://localhost:8080/v1/realtime";
		
		RestTemplate restTemplate = new RestTemplate();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		String clientIpAddress = "39.53.140.26";
		headers.add("X-FORWARDED-FOR", clientIpAddress );
		
		HttpEntity<String> request = new HttpEntity<>(headers);
		
		ResponseEntity<String> response = restTemplate.exchange(requestURI, HttpMethod.GET, request, String.class);
		
		HttpStatusCode statusCode = response.getStatusCode();
		System.out.println("Status code: " + statusCode );
		
		if(statusCode.value() == HttpStatus.OK.value()) {
			String body = response.getBody();
			System.out.println(body);
		}
	  
		
	
	}

}
