package com.wrapper.templates.exceptions;

import org.springframework.http.HttpMethod;
import com.wrapper.templates.Inputs;

public class IncompleteRESTServiceOutputException extends ExceptionDecorator {
	
	private static final long serialVersionUID = 1L;
	
	private final String message;
	private final String serviceName;
	private final String resourceName;
	private final Inputs inputs;
	private final HttpMethod httpMethod;
	private final String consumerMessage;
	private final Throwable exception;
	
	public static class Builder {
		private String withServiceName;
		private String withResourceName;
		private Inputs withInputs;
		private Exception withException;
		private HttpMethod throughHttpMethod;
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
		
		public Builder withResourceName(String withResourceName) {
			this.withResourceName = withResourceName;
			return this;
		}
		
		public String getWithResourceName() {
			return withResourceName;
		}
		
		public Builder throughHttpMethod(HttpMethod throughHttpMethod) {
			this.throughHttpMethod = throughHttpMethod;
			return this;
		}
		
		public HttpMethod getThroughHttpMethod() {
			return throughHttpMethod;
		}
		
		public IncompleteRESTServiceOutputException build() {
			return new IncompleteRESTServiceOutputException(this);
		}
	}
	
	public IncompleteRESTServiceOutputException(Builder builder) {
		serviceName = builder.getWithServiceName();
		resourceName = builder.getWithResourceName();
		httpMethod = builder.getThroughHttpMethod();
		inputs = builder.getWithInputs();
		exception = builder.getWithException();
		consumerMessage = builder.getWithMessageFromConsumer();
		StringBuilder sb = new StringBuilder();
		sb.append("Tried to reach rest service ").append(serviceName).append(" ").append(resourceName).append(" with inputs ")
						.append(inputs.toString()).append("through ").append(httpMethod.name()).append(" method, ");
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
	
	public String resourceName() {
		return resourceName;
	}
	
	public HttpMethod httpMethod() {
		return httpMethod;
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
