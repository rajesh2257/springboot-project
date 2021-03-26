package com.onpassive.omail.model;

import java.util.Date;
import java.util.List;

import org.springframework.http.HttpStatus;

public class ResponseMessage {

	private Date timeStamp;
	private HttpStatus status;
	private String message;
	private String path;
	private List<String> details;

	public Date getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Date timeStamp) {
		this.timeStamp = timeStamp;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public void setStatus(HttpStatus status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public List<String> getDetails() {
		return details;
	}

	public void setDetails(List<String> details) {
		this.details = details;
	}

	@Override
	public String toString() {
		return "ResponseMessage [timeStamp=" + timeStamp + ", status=" + status + ", message=" + message + ", path="
				+ path + ", details=" + details + "]";
	}

	public ResponseMessage() {

	}
	
	public ResponseMessage(Date timeStamp, HttpStatus status, String message, String path, List<String> details) {
		super();
		this.timeStamp = timeStamp;
		this.status = status;
		this.message = message;
		this.path = path;
		this.details = details;
	}

	public ResponseMessage(Date timeStamp, HttpStatus status, String path, List<String> details) {
		super();
		this.timeStamp = timeStamp;
		this.status = status;
		this.path = path;
		this.details = details;
	}

	public ResponseMessage(Date timeStamp, HttpStatus status, String message, String path) {
		super();
		this.timeStamp = timeStamp;
		this.status = status;
		this.message = message;
		this.path = path;
	}


}
