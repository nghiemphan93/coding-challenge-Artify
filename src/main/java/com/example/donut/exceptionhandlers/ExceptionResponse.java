package com.example.donut.exceptionhandlers;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Data
public class ExceptionResponse {
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
	private Date timestamp = new Date();
	private HttpStatus status;
	private String message;
	private String debugMessage;

	public ExceptionResponse(HttpStatus status, String message, String debugMessage) {
		this.status = status;
		this.message = message;
		this.debugMessage = debugMessage;
	}
}
