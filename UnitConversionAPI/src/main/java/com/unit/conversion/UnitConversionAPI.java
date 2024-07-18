package com.unit.conversion;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/unit-conversion")
public class UnitConversionAPI {

	@PostMapping
	public  ResponseEntity<ConverstionDetails> convert(@RequestBody ConverstionDetails details) {
		
		  try {
			UnitConvertor.convertor(details);
		} catch (UnitConversionException e) {
			
			return  ResponseEntity.badRequest().build();
		}
		  
		  return new ResponseEntity<ConverstionDetails>(details,HttpStatus.OK);
	}
	
	
}
