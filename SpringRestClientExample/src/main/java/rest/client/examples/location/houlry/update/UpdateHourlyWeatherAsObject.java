package rest.client.examples.location.houlry.update;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import rest.client.examples.location.houlry.HourlyWeather;

public class UpdateHourlyWeatherAsObject {

    public static void main(String[] args) {
        String requestURI = "http://localhost:8080/v1/hourly/{locationCode}";
        String locationCode = "APE_PK";
        
        RestTemplate restTemplate = new RestTemplate();
        
        HourlyWeather forecast1 = new HourlyWeather();
        forecast1.setHourOfDay(11);
        forecast1.setPrecipitation(10);
        forecast1.setStatus("Sunny");
        forecast1.setTemperature(27);
        
        HourlyWeather forecast2 = new HourlyWeather();
        forecast2.setHourOfDay(15);
        forecast2.setPrecipitation(50);
        forecast2.setStatus("Partly Cloudy");
        forecast2.setTemperature(28);
        
        HourlyWeather forecast3 = new HourlyWeather();
        forecast3.setHourOfDay(16);
        forecast3.setPrecipitation(40);
        forecast3.setStatus("Cloudy");
        forecast3.setTemperature(25);
        
        HourlyWeather[] hourlyForecast = new HourlyWeather[]{forecast1, forecast2, forecast3};
        
        HttpEntity<HourlyWeather[]> request = new HttpEntity<>(hourlyForecast);
        try {
            var response = restTemplate.exchange(requestURI, HttpMethod.PUT, request, Object.class, locationCode);
            HttpStatusCode statusCode = response.getStatusCode();
            System.out.println("Status code: " + statusCode);
        } catch (RestClientResponseException ex) {
            System.out.println("Error: " + ex.getStatusCode());
            ex.printStackTrace();
        }
    }
}
