package rest.client.examples.location.houlry.update;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class UpdateHourlyWeatherAsJSONString {

	public static void main(String[] args) {
		String requestURI = "http://localhost:8080/v1/hourly/APE_PK";
		
		RestTemplate restTemplate = new RestTemplate();
		
		String json = """
					[
						  {
						    "hour_of_day": 5,
						    "temperature": 12,
						    "precipitation": 88,
						    "status": "Cloudy"
						  },
						  {
						    "hour_of_day": 7,
						    "temperature": 13,
						    "precipitation": 86,
						    "status": "Cloudy"
						  },
						  {
						    "hour_of_day": 13,
						    "temperature": 15,
						    "precipitation": 60,
						    "status": "Sunny"
						  },
						   {
						    "hour_of_day": 12,
						    "temperature": 15,
						    "precipitation": 60,
						    "status": "Sunny"
						  }
             	]
				""";
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		HttpEntity<String> request = new HttpEntity<>(json,headers);
		try {
			restTemplate.put(requestURI, request, headers);
			System.out.println("Hourly forecast is updated..");
		} catch(RestClientResponseException ex ) {
			System.out.println(ex.getStatusCode());
		}
		
	}
}
