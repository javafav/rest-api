package com.weight.conversion;

public class UnitConvertor {

	public static void convertor(ConverstionDetails details) throws WeightConverterException {
		
		String fromUnit = details.getFromUnit();
		String toUnit = details.getToUnit();
		
		if(fromUnit.equals("kg") && toUnit.equals("pound")) {
			
			WeightConvertor.kg2Pound(details);
			
		} else if(fromUnit.equals("pound") && toUnit.equals("kg")) {
			
			WeightConvertor.pound2Kg(details);
			
		} else {
			throw new WeightConverterException("Invlaid fromUnit Or toUnit");
		}
		
	}
}
