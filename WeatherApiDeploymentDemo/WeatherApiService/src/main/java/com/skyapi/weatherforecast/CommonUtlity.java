package com.skyapi.weatherforecast;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.http.HttpServletRequest;

public class CommonUtlity {

	public static final Logger LOGGER = LoggerFactory.getLogger(CommonUtlity.class);
	
	public static String getIPAddress(HttpServletRequest request) {
		String ipAddress = request.getHeader("X-FORWARDED-FOR");
		
		if(ipAddress == null || ipAddress.isEmpty()) {
			ipAddress = request.getRemoteAddr();
			return ipAddress;
		}
		LOGGER.info("Client IP Address: " + ipAddress);
		return ipAddress;
	}
}
