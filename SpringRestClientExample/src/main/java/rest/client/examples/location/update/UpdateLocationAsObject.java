package rest.client.examples.location.update;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import rest.client.examples.location.Location;



public class UpdateLocationAsObject {

	public static void main(String[] args) {
		String requestURI = "http://localhost:8080/v1/locations";
		RestTemplate restTemplate = new RestTemplate();
	
		Location location = new Location();
		location.setCode("Test_PK");
		location.setCityName("Uch Sharif");
		location.setRegionName("Bahwalpur");
		location.setCountryCode("PK");
		location.setCountryName("Pakistan");
		location.setEnabled(true);

		 
		try {
			restTemplate.put(requestURI, location);
			System.out.println("Location Updated...");
		} catch (RestClientResponseException ex) {
			System.out.println(ex.getStatusCode());
			ex.printStackTrace();
		}

	}
}
