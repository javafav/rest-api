package rest.client.examples.location.houlry.get;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import rest.client.examples.location.houlry.HourlyForecastDTO;

public class GetHourlyWeatherByCodeAsJSONString {

	public static void main(String[] args) {
		String requestURI = "http://localhost:8080/v1/hourly/{code}";
		String locationCode = "UCH_PK";
		
		RestTemplate restTemplate = new RestTemplate();
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		
		headers.add("X-CURRENT-HOUR", "1");
		
		var request = new HttpEntity<HourlyForecastDTO>(headers);
		
		var response = restTemplate.exchange(
				requestURI, HttpMethod.GET, request, HourlyForecastDTO.class, locationCode);
		
		HttpStatusCode statusCode = response.getStatusCode();
		
		System.out.println("Status code: " + statusCode);
		
		if (statusCode.value() == HttpStatus.NO_CONTENT.value()) {
			System.out.println("No hourly forecast data available");
			
		} else if (statusCode.value() == HttpStatus.OK.value()) {
			HourlyForecastDTO dto = response.getBody();
			System.out.println("Location: " + dto.getLocation());
			
			var hourlyForecast = dto.getHourlyForecast();
			hourlyForecast.forEach(System.out::println);
		} 
	}

}
