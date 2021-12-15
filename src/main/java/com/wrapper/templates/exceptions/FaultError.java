package com.wrapper.templates.exceptions;

@SuppressWarnings("serial")
public class FaultError extends Exception {

	private final String message;
	private final ErrorType errorType;
	private final String serviceCalledURL;
	private final String destinationName;
	private final Throwable exception;
	private final String consumerMessage;
	private Process process;
	
	public static class Builder {
		private ErrorType the;
		private String calling;
		private Throwable withException;
		private String withMessageFromConsumer;
		private String toDestination;
		private Process occurredWhile;
		
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

		public ErrorType getThe() {
			return the;
		}

		public Builder the(ErrorType the) {
			this.the = the;
			return this;
		}

		public String getCalling() {
			return calling;
		}

		public Builder calling(String calling) {
			this.calling = calling;
			return this;
		}

		public Process getOccurredWhile() {
			return occurredWhile;
		}
		
		public Builder occurredWhile(Process occurredWhile) {
			this.occurredWhile = occurredWhile;
			return this;
		}
		
		public FaultError build() {
			return new FaultError(this);
		}
	}

	public FaultError(Builder builder) {
		errorType = builder.getThe();
		process = builder.getOccurredWhile();
		exception = builder.getWithException();
		consumerMessage = builder.getWithMessageFromConsumer();
		destinationName = builder.getToDestination();
		serviceCalledURL = builder.getCalling();
		StringBuilder sb = new StringBuilder();
		sb.append(errorType.getErrorTypeMessage());
		sb.append(process.getProcessErrorMessage());
		/*if(ErrorType.JMS_ERROR.equals(errorType)) {
			sb.append(destinationName);
		} else {
			sb.append(serviceCalledURL);
		}
		sb.append(" failed with exception :");*/
		if(exception != null) {
			sb.append(exception.getMessage());
		} else {
			sb.append(consumerMessage);
		}
		sb.append("we can't continue with this issue.");;
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
	
	public Process process() {
		return process;
	}
}
