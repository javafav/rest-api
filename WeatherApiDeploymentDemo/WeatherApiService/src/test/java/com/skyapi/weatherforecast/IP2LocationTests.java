package com.skyapi.weatherforecast;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import com.ip2location.IP2Location;
import com.ip2location.IPResult;

public class IP2LocationTests {

	String  DBPath = "iploc2db/IP2LOCATION-LITE-DB3.BIN";
	
	@Test
	public void testIpInvalid() throws IOException {
		
	
		String ipAddress = "123";
		
		IP2Location ip2Location = new IP2Location();
		ip2Location.Open(DBPath);
		
		IPResult ipQuery = ip2Location.IPQuery(ipAddress);
		
		assertThat(ipQuery.getStatus().equals("INVALID_IP_ADDRESS"));
		
		
	}
	
	@Test
	public void testIpValidP1() throws IOException {
		
	
		String ipAddress = "39.53.140.26";
		
		IP2Location ip2Location = new IP2Location();
		ip2Location.Open(DBPath);
		
		IPResult ipQuery = ip2Location.IPQuery(ipAddress);
		
		assertThat(ipQuery.getStatus().equals("OK"));
		assertThat(ipQuery.getCity()).isEqualTo("Hasilpur");
		assertThat(ipQuery.getRegion()).isEqualTo("Punjab");
		System.out.println(ipQuery);
		
		
	}
	
	
	@Test
	public void testIpValidP2() throws IOException {
		
	
		String ipAddress = "103.151.43.104";
		
		IP2Location ip2Location = new IP2Location();
		ip2Location.Open(DBPath);
		
		IPResult ipQuery = ip2Location.IPQuery(ipAddress);
		
		assertThat(ipQuery.getStatus().equals("OK"));
		assertThat(ipQuery.getCity()).isEqualTo("Lahore");
		assertThat(ipQuery.getRegion()).isEqualTo("Punjab");
		System.out.println(ipQuery);
		
		
	}
	
}
