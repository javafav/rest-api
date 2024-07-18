package com.weight.conversion;

public class WeightConvertor {

	public static void kg2Pound(ConverstionDetails details) {
		
		float kg = details.getFromValue();
		float pound = kg * 2.20462f;
		
		details.setToValue(pound);
	
	}
	
	public static void pound2Kg(ConverstionDetails details) {
		
		float pound = details.getFromValue();
		float kg = pound * 0.453592f;
		
		details.setToValue(kg);
		
	}
}
