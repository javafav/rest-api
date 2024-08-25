package rest.client.examples.location.list;

import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class ListLocationsAsJSONString {

	public static void main(String[] args) {
		String requestURI = "http://localhost:8080/v1/location";
		try {
		RestTemplate restTemplate = new RestTemplate();
		String response = restTemplate.getForObject(requestURI, String.class);
		
		System.out.println(response);
		}catch(RestClientResponseException ex) {
			System.out.println("Status Code:" + ex.getStatusCode());
			ex.printStackTrace();
		}

	}

}
