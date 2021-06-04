package com.wrapper.templates.exceptions;

@SuppressWarnings("serial")
public class ApplicationException extends Exception {

	private final String message;

	public enum Process {
		BUILDING_URL("but building service call URL failed with exception :"),
		BUILDING_HEADERS("but building service request failed with exception :"),
		BUILDING_REQUEST("but building request headers failed with exception :"),
		MAPPING_RESPONSE_TO_RESPONSESTATUS("but mapping response to response status failed with exception :"),
		MAPPING_RESPONSE_TO_MODEL("but mapping response with model failed with exception :"),
		BUILDING_PROCEDURE_PARAMETERS("but building stored proc input,output parameters failed with exception :"),
		BUILDING_SQLQUERY_PARAMETERS("but building sql query input,output parameters failed with exception :"), 
		MAPPING_OUTPUT_TO_RESPONSESTATUS("but mapping output to response status failed with exception :"),
		MAPPING_OUTPUT_TO_RESPONSE("but mapping output to model failed with exception :");

		private final String processErrorMessage;

		Process(String processErrorMessage) {
			this.processErrorMessage = processErrorMessage;
		}

		public String getProcessErrorMessage() {
			return this.processErrorMessage;
		}
	}
	
	private final Exception exception;
	private final Process process;
	private final String messageFromConsumer;
	
	public static class Builder {
		private Exception withCauseForException;
		private Process occurredWhile;
		private String messageFromConsumer;
		
		public Builder(Exception exception) {
			this.withCauseForException = exception;
		}
		
		public Exception getWithCauseForException() {
			return withCauseForException;
		}
		
		public Builder(String messageFromConsumer) {
			this.messageFromConsumer = messageFromConsumer;
		}
		
		public String getMessageFromConsumer() {
			return messageFromConsumer;
		}
		
		public Process getOccurredWhile() {
			return occurredWhile;
		}
		
		public Builder occurredWhile(Process occurredWhile) {
			this.occurredWhile = occurredWhile;
			return this;
		}
		
		public Builder occurredWhile(String messageFromConsumer) {
			this.messageFromConsumer = messageFromConsumer;
			return this;
		}
		
		public ApplicationException build() {
			return new ApplicationException(this);
		}
	}
	
	public ApplicationException(Builder builder) {
		exception = builder.getWithCauseForException();
		process = builder.getOccurredWhile();
		messageFromConsumer = builder.getMessageFromConsumer();
		StringBuilder sb = new StringBuilder();
		sb.append(process.getProcessErrorMessage());
		if(exception != null) {
			sb.append(exception.getMessage());
		} else {
			sb.append(messageFromConsumer);
		}
		message = sb.toString();
	}
	
	@Override
	public String getMessage() {
		return message;
	}
	
	public Exception exception() {
		return exception;
	}
	
	public Process process() {
		return process;
	}
	
	public String messageFromConsumer() {
		return messageFromConsumer;
	}
}
