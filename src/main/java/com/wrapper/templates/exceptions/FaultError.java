package com.wrapper.templates.exceptions;

@SuppressWarnings("serial")
public class FaultError extends Exception {

	private final String message;

	public enum ErrorType {
		JMS_ERROR("but posting message to destination "),
		REST_ERROR("but calling resource "),
		SOAP_ERROR("but calling operation "),
		STOREDPROC_ERROR("but executing stored proc on "),
		SQLQUERY_ERROR("but executing sql query on "),
		NO_SQL_ERROR("but executing no sql query on ");

		private final String errorTypeMessage;

		ErrorType(String errorTypeMessage) {
			this.errorTypeMessage = errorTypeMessage;
		}

		public String getErrorTypeMessage() {
			return this.errorTypeMessage;
		}
	}

	private final ErrorType errorType;
	private final String serviceCalledURL;
	private final String destinationName;
	private final Throwable exception;
	private final String consumerMessage;
	
	public static class Builder {
		private ErrorType theError;
		private String occurredWhileCalling;
		private Throwable withException;
		private String withMessageFromConsumer;
		private String toDestination;
		
		public Builder(Throwable withException) {
			this.withException = withException;
		}

		public Throwable getWithException() {
			return withException;
		}
		
		public Builder(String withMessageFromConsumer) {
			this.withMessageFromConsumer = withMessageFromConsumer;
		}

		public String getWithMessageFromConsumer() {
			return withMessageFromConsumer;
		}
		
		public String getToDestination() {
			return toDestination;
		}

		public Builder toDestination(String toDestination) {
			this.toDestination = toDestination;
			return this;
		}

		public ErrorType getTheError() {
			return theError;
		}

		public Builder theError(ErrorType theError) {
			this.theError = theError;
			return this;
		}

		public String getOccurredWhileCalling() {
			return occurredWhileCalling;
		}

		public Builder withOccurredWhileCalling(String occurredWhileCalling) {
			this.occurredWhileCalling = occurredWhileCalling;
			return this;
		}

		public FaultError build() {
			return new FaultError(this);
		}
	}

	public FaultError(Builder builder) {
		errorType = builder.getTheError();
		serviceCalledURL = builder.getOccurredWhileCalling();
		exception = builder.getWithException();
		consumerMessage = builder.getWithMessageFromConsumer();
		destinationName = builder.getToDestination();
		StringBuilder sb = new StringBuilder();
		sb.append(errorType.getErrorTypeMessage());
		if(ErrorType.JMS_ERROR.equals(errorType)) {
			sb.append(destinationName);
		} else {
			sb.append(serviceCalledURL);
		}
		sb.append(" failed with exception :");
		if(exception != null) {
			sb.append(exception.getMessage());
		} else {
			sb.append(consumerMessage);
		}
		message = sb.toString();
	}

	public String getMessage() {
		return message;
	}

	public ErrorType errorType() {
		return errorType;
	}

	public String destinationName() {
		return destinationName;
	}

	public String serviceCalledURL() {
		return serviceCalledURL;
	}

	public Throwable exception() {
		return exception;
	}

	public String consumerMessage() {
		return consumerMessage;
	}
}
