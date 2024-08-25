package rest.client.examples.location.update;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class UpdateLocationAsJSONString {

	public static void main(String[] args) {
		String requestURI = "http://localhost:8080/v1/locations";
		RestTemplate restTemplate = new RestTemplate();

		String json = """
					   		{
						  "code": "LACA_USA",
						  "city_name": "Los Angeles",
						  "region_name": "California",
						  "country_code": "US",
						  "country_name": "United States of America",
						  "enabled": false
				}
				  		""";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> request = new HttpEntity<>(json, headers);
		
		try {
			restTemplate.put(requestURI, request, headers);
			System.out.println("Location Updated...");
		} catch (RestClientResponseException ex) {
			System.out.println(ex.getStatusCode());
			ex.printStackTrace();
		}

	}
}
