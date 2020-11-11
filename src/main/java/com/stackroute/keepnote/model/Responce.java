package com.stackroute.keepnote.model;

public class Responce {
	
	private int status;
	private String message;
	
	public Responce() {	
	}

	public Responce(int status, String message) {
		super();
		this.status = status;
		this.message = message;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}	

}
