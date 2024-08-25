package rest.client.examples.location.houlry.get;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import rest.client.examples.location.houlry.HourlyForecastDTO;

public class GetHourlyWeatherByCodeAsObject {

	public static void main(String[] args) {
		String requestURI = "http://localhost:8080/v1/hourly";
		
		RestTemplate restTemplate = new RestTemplate();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		String clientIpAddress = "39.53.140.26";
		
		headers.add("X-FORWARDED-FOR", clientIpAddress);
		headers.add("X-CURRENT-HOUR", "6");
		
		HttpEntity<String> request = new HttpEntity<String>(headers);
		
		ResponseEntity<HourlyForecastDTO> response = restTemplate.exchange(requestURI, HttpMethod.GET, request, HourlyForecastDTO.class);
		
		HttpStatusCode statusCode = response.getStatusCode();
		
		if(statusCode.value() ==  HttpStatus.OK.value()) {
			HourlyForecastDTO body = response.getBody();
			
			System.out.println(body);
		}
		
		
	}
}
