package rest.client.examples.location.houlry.get;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

public class GetHourlyWeatherByIPAsJSONString {

	public static void main(String[] args) {
		String requestURI = "http://localhost:8080/v1/hourly";
		
		RestTemplate restTemplate = new RestTemplate();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		String clientIpAddress = "39.53.140.26";
		
		headers.add("X-CURRENT-HOUR", "14");
		headers.add("X-FORWARDED-FOR", clientIpAddress);	// Bahwalpur, Pak
		
		var request = new HttpEntity<String>(headers);
		
		var response = restTemplate.exchange(requestURI, HttpMethod.GET, request, String.class);
		
		HttpStatusCode statusCode = response.getStatusCode();
		
		System.out.println("Status code: " + statusCode);
		
		if (statusCode.value() == HttpStatus.NO_CONTENT.value()) {
			System.out.println("No hourly forecast data available");
			
		} else if (statusCode.value() == HttpStatus.OK.value()) {
			String body = response.getBody();
			System.out.println("Location: " + body);
			
		} 
	}

}
