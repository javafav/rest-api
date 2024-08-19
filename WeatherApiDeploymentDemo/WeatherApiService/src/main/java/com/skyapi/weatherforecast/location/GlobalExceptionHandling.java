package com.skyapi.weatherforecast.location;

import java.util.Date;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.skyapi.weatherforecast.hourly.BadRequestException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class GlobalExceptionHandling extends ResponseEntityExceptionHandler {

    @Autowired private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandling.class);
	
    
    

    
    
    
    
    @ExceptionHandler(BadRequestException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorDTO  handleBadRequestException(HttpServletRequest request, BadRequestException ex) {
	
		ErrorDTO error = new ErrorDTO();
		
		error.setTimestamp(new Date());
		error.setStatus(HttpStatus.BAD_REQUEST.value());
		error.addErrors(ex.getMessage());
		error.setPath(request.getServletPath());
		
		LOGGER.error(ex.getMessage(), ex);
		
		return error;
		
		
	}
    
    @ExceptionHandler(LocationNotFoundException.class)
  	@ResponseStatus(HttpStatus.NOT_FOUND)
  	@ResponseBody
  	public ErrorDTO  handleLocationNorFoundException(HttpServletRequest request, LocationNotFoundException ex) {
  	
  		ErrorDTO error = new ErrorDTO();
  		
  		error.setTimestamp(new Date());
  		error.setStatus(HttpStatus.NOT_FOUND.value());
  		error.addErrors(ex.getMessage());
  		error.setPath(request.getServletPath());
  		
  		LOGGER.error(ex.getMessage(), ex);
  		
  		return error;
  		
  		
  	}
      
    
    
    @ExceptionHandler(ConstraintViolationException.class)
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ResponseBody
	public ErrorDTO  handleConstraintViolationException(HttpServletRequest request, ConstraintViolationException ex) {
    //	ConstraintViolationException ex =  (ConstraintViolationException) e;
		ErrorDTO error = new ErrorDTO();
		var constraintViolations = ex.getConstraintViolations();
		
		error.setTimestamp(new Date());
		error.setStatus(HttpStatus.BAD_REQUEST.value());
		error.setPath(request.getServletPath());
		
		
		constraintViolations.forEach(constraint -> {
			error.addErrors(constraint.getPropertyPath() + ": " + constraint.getMessage());
		} );
		
	
		
		LOGGER.error(ex.getMessage(), ex);
		
		return error;
		
		
	}
    
    
    

    
    
    @ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ResponseBody
	public ErrorDTO handleGenricException(HttpServletRequest request, Exception ex) {
	
		ErrorDTO error = new ErrorDTO();
		
		error.setTimestamp(new Date());
		error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
		error.addErrors(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
		error.setPath(request.getServletPath());
		
		LOGGER.error(ex.getMessage(), ex);
		
		return error;
		
		
	}
    

      
    

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatusCode status, WebRequest request) {

		ErrorDTO error = new ErrorDTO();
		
		LOGGER.error(ex.getMessage(), ex);
		
		error.setTimestamp(new Date());
		error.setStatus(HttpStatus.BAD_REQUEST.value());
	    error.setPath( ((ServletWebRequest)request).getRequest().getServletPath() );
		
		List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();
 
		fieldErrors.forEach(fieledError -> {
			error.addErrors(fieledError.getDefaultMessage());
		});
		
		return new ResponseEntity<>(error,headers,status);
	}
	
	
	
}
