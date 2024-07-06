package com.helloworld;

import java.util.Date;

public class Response {

	private String message;
	private String date;
	public String getMessage() {
		return message;
	}
	
	
	public Response(String message) {
        
		this.message = message;
		date = new Date().toString();
	}


	public void setMessage(String message) {
		this.message = message;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
	
}
