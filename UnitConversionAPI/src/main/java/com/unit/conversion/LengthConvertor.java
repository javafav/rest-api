package com.unit.conversion;

public class LengthConvertor {

	public static void kilometer2Mile(ConverstionDetails details) {
		float km = details.getFromValue();
		float miles = km * 0.621371f;
		
		details.setToValue(miles);
	}
	
	public static void mile2Kilometer(ConverstionDetails details) {
		float mile = details.getFromValue();
		float km = mile * 1.60934f;
		
		details.setToValue(km);
	}
}
