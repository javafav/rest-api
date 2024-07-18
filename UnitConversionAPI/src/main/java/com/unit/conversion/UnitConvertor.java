package com.unit.conversion;

public class UnitConvertor {

	public static void convertor(ConverstionDetails details) throws UnitConversionException {
		
		String fromUnit = details.getFromUnit();
		String toUnit = details.getToUnit();
		
		if(fromUnit.equals("km") && toUnit.equals("mile") ) {
			
			LengthConvertor.kilometer2Mile(details);
		
		} else if(fromUnit.equals("mile") && toUnit.equals("km") ) {
			
			LengthConvertor.mile2Kilometer(details);
			
		} else {
			throw new UnitConversionException("Invalid fromUnit or toUnit");
		}
		
	}
}
