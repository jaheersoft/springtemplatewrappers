package com.wrapper.templates.exceptions;

import com.wrapper.templates.Inputs;

public class IncompleteSOAPServiceOutputException extends ExceptionDecorator {
	
	private static final long serialVersionUID = 1L;
	
	private final String message;
	private final String serviceName;
	private final String operationName;
	private final Inputs inputs;
	private final String consumerMessage;
	private final Throwable exception;
	
	public static class Builder {
		private String withServiceName;
		private String withOperationName;
		private Inputs withInputs;
		private Exception withException;
		private String withMessageFromConsumer;
		
		public Builder(Exception withException) {
			this.withException = withException;
		}
		
		public Exception getWithException() {
			return withException;
		}
		
		public Builder(String withMessageFromConsumer) {
			this.withMessageFromConsumer = withMessageFromConsumer;
		}
		
		public String getWithMessageFromConsumer() {
			return withMessageFromConsumer;
		}
		
		public Builder withInputs(Inputs withInputs) {
			this.withInputs = withInputs;
			return this;
		}
		
		public Inputs getWithInputs() {
			return withInputs;
		}
		
		public Builder withServiceName(String withServiceName) {
			this.withServiceName = withServiceName;
			return this;
		}
		
		public String getWithServiceName() {
			return withServiceName;
		}
		
		public Builder withOperationName(String withOperationName) {
			this.withOperationName = withOperationName;
			return this;
		}
		
		public String getWithOperationName() {
			return withOperationName;
		}
		
		public IncompleteSOAPServiceOutputException build() {
			return new IncompleteSOAPServiceOutputException(this);
		}
	}
	
	public IncompleteSOAPServiceOutputException(Builder builder) {
		serviceName = builder.getWithServiceName();
		operationName = builder.getWithOperationName();
		inputs = builder.getWithInputs();
		exception = builder.getWithException();
		consumerMessage = builder.getWithMessageFromConsumer();
		StringBuilder sb = new StringBuilder();
		sb.append("Tried to reach soap service ").append(serviceName).append(" ").append(operationName).append(" with inputs ")
						.append(inputs.toString()).append(", ");
		if(exception != null) {
			sb.append(exception.getMessage());
		} else {
			sb.append(consumerMessage);
		}
		message = sb.toString();
	}
	
	public String serviceName() {
		return serviceName;
	}
	
	public String operationName() {
		return operationName;
	}
	
	public String consumerMessage() {
		return consumerMessage;
	}
	
	public Inputs inputs() {
		return inputs;
	}
	
	public Throwable exception() {
		return exception;
	}
	
	@Override
	public String getMessage() {
		return message;
	}
}