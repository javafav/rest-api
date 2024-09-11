package com.student;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.student.security.jwt.JwtValidationException;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandling extends ResponseEntityExceptionHandler  {

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
    
    @ExceptionHandler(JwtValidationException.class)
 	@ResponseStatus(HttpStatus.UNAUTHORIZED)
 	@ResponseBody
 	public ErrorDTO handleJwtValidationException(HttpServletRequest request, Exception ex) {
 	
 		ErrorDTO error = new ErrorDTO();
 		
 		error.setTimestamp(new Date());
 		error.setStatus(HttpStatus.UNAUTHORIZED.value());
 		error.addErrors(ex.getMessage());
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
			error.addErrors(fieledError.getField() + ": " + fieledError.getDefaultMessage());
		});
		
		return new ResponseEntity<>(error,headers,status);
	}
	
	
	
	
	
	
}
