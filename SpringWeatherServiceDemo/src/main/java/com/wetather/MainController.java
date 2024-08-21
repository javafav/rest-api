package com.wetather;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

	@Autowired
	private WeatherService service;

	@GetMapping("")
	public String viewHome(Model model) {
		try {
			RealtimeWeather realtimeWeather = service.getForecasat();
			model.addAttribute("weather", realtimeWeather);

			return "index";
			
		} catch (WeatherServiceException e) {
			
			model.addAttribute("message", e.getMessage());
			return "error";

		}
	}
}
