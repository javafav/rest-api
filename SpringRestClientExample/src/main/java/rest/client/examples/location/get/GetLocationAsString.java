package rest.client.examples.location.get;

import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class GetLocationAsString {

	public static void main(String[] args) {
	
		String requestURI  = "http://localhost:8080/v1/locations/{code}";
		 HashMap<String, String> map = new HashMap<String, String>();
		 map.put("code", "APE_PK");
		
		RestTemplate restTemplate = new RestTemplate();
		
		ResponseEntity<String> response = restTemplate.getForEntity(requestURI, String.class, map);
		HttpStatusCode statusCode = response.getStatusCode();
		
		System.out.println("Http status code: " + statusCode);
		
		if(statusCode.value() == HttpStatus.OK.value()) {
			String location = response.getBody();
			System.out.println(location);
		}
	
	
	
	
	}
}
