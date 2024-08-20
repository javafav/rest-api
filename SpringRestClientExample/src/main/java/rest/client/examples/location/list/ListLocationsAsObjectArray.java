package rest.client.examples.location.list;

import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.UnknownHttpStatusCodeException;

import rest.client.examples.location.Location;

public class ListLocationsAsObjectArray {

	public static void main(String[] args) {
		String requestURI = "http://localhost:8080/v1/locations";
		RestTemplate restTemplate = new RestTemplate();
		try {

			Location[] locations = restTemplate.getForObject(requestURI, Location[].class);

			if (locations != null && locations.length > 0) {

				for (Location location : locations) {

					System.out.println(location);

				}
			}

		}  catch (HttpClientErrorException ex) {
			System.out.println("Client error: " + ex.getStatusCode() + "-" + ex.getStatusText());
			ex.printStackTrace();
		} catch(HttpServerErrorException ex) {
			System.out.println("Server error: " + ex.getStatusCode() + "-" + ex.getStatusText());
			ex.printStackTrace();
		} catch(UnknownHttpStatusCodeException ex ) {
			System.out.println("Server error: " + ex.getStatusCode() + "-" + ex.getStatusText());
			ex.printStackTrace();
		}

	}

}
