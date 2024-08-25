package rest.client.examples.location.add;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import rest.client.examples.location.Location;


public class AddLocationAsObject {

	public static void main(String[] args) {

		String requestURI = "http://localhost:8080/v1/locations";
		RestTemplate restTemplate = new RestTemplate();
		
		Location location = new Location();
		location.setCode("APE_PK");
		location.setCityName("Ahmad Pur East");
		location.setRegionName("Bahwalpur");
		location.setCountryCode("PK");
		location.setCountryName("Pakistan");
		location.setEnabled(true);



		HttpEntity<Location> request = new HttpEntity<Location>(location);

		ResponseEntity<Location> response = restTemplate.postForEntity(requestURI, request, Location.class);
		HttpStatusCode statusCode = response.getStatusCode();
		
		System.out.println("Status Code: " + statusCode);

		Location addedLocation = response.getBody();
		
		System.out.println(addedLocation);

	}
}
