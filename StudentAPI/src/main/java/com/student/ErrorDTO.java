package com.student;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ErrorDTO {

	private Date timestamp;
	private int status;
	private String path;
	private List<String> errors = new ArrayList<>();

	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int statusCode) {
		this.status = statusCode;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public List<String> getErrors() {
		return errors;
	}

	public void setErrors(List<String> errors) {
		this.errors = errors;
	}

 
	public void addErrors(String error) {
		this.errors.add(error);
	}

}
