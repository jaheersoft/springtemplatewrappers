package com.wrapper.templates.exceptions;

public enum ErrorType {
	JMS_ERROR("but while processing jms call, "),
	REST_ERROR("but while processing rest call, "),
	SOAP_ERROR("but while processing soap call, "),
	STOREDPROC_ERROR("but while processing stored proc call, "),
	SQLQUERY_ERROR("but while processing sql query call, "),
	NOSQL_ERROR("but while processing no sql query call, ");

	private final String errorTypeMessage;

	ErrorType(String errorTypeMessage) {
		this.errorTypeMessage = errorTypeMessage;
	}

	public String getErrorTypeMessage() {
		return this.errorTypeMessage;
	}
}
