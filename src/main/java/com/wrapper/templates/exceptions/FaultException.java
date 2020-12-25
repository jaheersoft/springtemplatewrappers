package com.wrapper.templates.exceptions;

@SuppressWarnings("serial")
public class FaultException extends Exception {

	public enum ErrorType {
		REST_ERROR(""), SOAP_ERROR(""),ORACLE_ERROR(""),MONGO_ERROR("");

		private final String errorMessage;

		ErrorType(String errorMessage) {
			this.errorMessage = errorMessage;
		}

		public String getErrorMessage() {
			return this.errorMessage;
		}
	}

	private final String message;

	public static class Builder {
		private ErrorType forErrorType;
		private String[] consumerInputs;
		private String withServiceCalledURL;
		private Throwable withCauseForException;
		private String messageFromConsumer;

		public Builder(Throwable withCauseForException) {
			this.withCauseForException = withCauseForException;
		}

		public Builder(String messageFromConsumer) {
			this.messageFromConsumer = messageFromConsumer;
		}

		public ErrorType getForErrorType() {
			return forErrorType;
		}

		public Builder forErrorType(ErrorType forErrorType) {
			this.forErrorType = forErrorType;
			return this;
		}

		public String[] getConsumerInputs() {
			return consumerInputs;
		}

		public Builder consumerInputs(String... consumerInputs) {
			this.consumerInputs = consumerInputs;
			return this;
		}

		public String getWithServiceCalledURL() {
			return withServiceCalledURL;
		}

		public Builder withServiceCalledURL(String withServiceCalledURL) {
			this.withServiceCalledURL = withServiceCalledURL;
			return this;
		}

		public Throwable getWithCauseForException() {
			return withCauseForException;
		}

		public String getMessageFromConsumer() {
			return messageFromConsumer;
		}

		public FaultException build() {
			return new FaultException(this);
		}
	}

	public FaultException(Builder builder) {
		StringBuilder sb = new StringBuilder();
		sb.append(builder.getForErrorType().getErrorMessage());
		for(String consumerInput : builder.getConsumerInputs()) {
			sb.append(consumerInput);
		}
		sb.append(builder.getWithServiceCalledURL());
		if (builder.getWithCauseForException() != null) {
			sb.append(builder.getWithCauseForException().getMessage());
		} else {
			sb.append(builder.getMessageFromConsumer());
		}
		message = sb.toString();
	}

	public String getMessage() {
		return message;
	}
}
