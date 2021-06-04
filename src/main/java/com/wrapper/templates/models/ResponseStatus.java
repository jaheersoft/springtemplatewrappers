package com.wrapper.templates.models;

import java.math.BigDecimal;
import java.util.List;

public final class ResponseStatus {
	
	private String status;
	private String statusText;
	private List<Message> messages;
	private BigDecimal errorCode;
	
	public String getStatus() {
		return status;
	}
	
	public ResponseStatus status(String status) {
		this.status = status;
		return this;
	}
	
	public String getStatusText() {
		return statusText;
	}
	
	public ResponseStatus statusText(String statusText) {
		this.statusText = statusText;
		return this;
	}
	
	public List<Message> getMessages() {
		return messages;
	}
	
	public ResponseStatus messages(List<Message> messages) {
		this.messages = messages;
		return this;
	}
	
	public BigDecimal getErrorCode() {
		return errorCode;
	}
	
	public ResponseStatus errorCode(BigDecimal errorCode) {
		this.errorCode = errorCode;
		return this;
	}
}
