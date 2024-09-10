package com.student;

import java.util.Date;

import org.slf4j.Logger;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandling  {

    @Autowired private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandling.class);

    @ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorDTO handleConstraintViolationException(HttpServletRequest request, Exception ex) {
	
		ErrorDTO error = new ErrorDTO();
		
		error.setTimestamp(new Date());
		error.setStatus(HttpStatus.BAD_REQUEST.value());
		error.addErrors(ex.getMessage());
		error.setPath(request.getServletPath());
		
		LOGGER.error(ex.getMessage(), ex);
		
		return error;
		
		
	}
	
	
	
	
	
	
}
