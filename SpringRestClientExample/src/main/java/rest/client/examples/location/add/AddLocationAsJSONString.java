package rest.client.examples.location.add;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class AddLocationAsJSONString {

	public static void main(String[] args) {

		String requestURI = "http://localhost:8080/v1/locations";
		RestTemplate restTemplate = new RestTemplate();

		String jsonObject = """
				    		{
				  "code": "LACA_US",
				  "city_name": "Los Angeles",
				  "region_name": "California",
				  "country_code": "US",
				  "country_name": "United States of America",
				  "enabled": true
				}
				    		""";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<String> request = new HttpEntity<String>(jsonObject, headers);

		ResponseEntity<String> response = restTemplate.postForEntity(requestURI, request, String.class);
		HttpStatusCode statusCode = response.getStatusCode();
		
		System.out.println("Status Code: " + statusCode);

		String addedLocation = response.getBody();
		
		System.out.println(addedLocation);

	}
}
