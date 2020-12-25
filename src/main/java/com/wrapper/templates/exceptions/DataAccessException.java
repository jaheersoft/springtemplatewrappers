package com.wrapper.templates.exceptions;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("serial")
public class DataAccessException extends ExceptionDecorator {
    private Map<String, String> failedRecords = new HashMap<>();
    Exception exception;
    private String currentMessage;

    public DataAccessException() {
        super();
    }

    public DataAccessException(Exception exception) {
        this.exception = exception;
    }

    public DataAccessException(String currentMessage, Map<String, String> failedRecords) {
    	this.currentMessage = currentMessage;
        this.failedRecords = failedRecords;
    }

    public Map<String, String> getFailedRecords() {
        return failedRecords;
    }

	@Override
	public String getMessage() {
		return currentMessage+exception.getMessage()+failedRecords.entrySet().toString();
	}

}
