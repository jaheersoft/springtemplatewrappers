package com.wrapper.templates.exceptions;

@SuppressWarnings("serial")
public class ApplicationException extends Exception {

	private final String message;
	private final Exception exception;
	private final Process process;
	private final ErrorType errorType;
	private final String messageFromConsumer;
	
	public static class Builder {
		
		private ErrorType the;
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
		
		public ErrorType getThe() {
			return the;
		}

		public Builder the(ErrorType the) {
			this.the = the;
			return this;
		}
		
		public Process getOccurredWhile() {
			return occurredWhile;
		}
		
		public Builder occurredWhile(Process occurredWhile) {
			this.occurredWhile = occurredWhile;
			return this;
		}
		
		public ApplicationException build() {
			return new ApplicationException(this);
		}
	}
	
	public ApplicationException(Builder builder) {
		exception = builder.getWithCauseForException();
		process = builder.getOccurredWhile();
		errorType = builder.getThe();
		messageFromConsumer = builder.getMessageFromConsumer();
		StringBuilder sb = new StringBuilder();
		sb.append(errorType.getErrorTypeMessage());
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
	
	public ErrorType errorType() {
		return errorType;
	}
}
