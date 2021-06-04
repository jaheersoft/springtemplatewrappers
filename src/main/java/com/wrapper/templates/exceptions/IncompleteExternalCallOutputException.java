package com.wrapper.templates.exceptions;

import com.wrapper.templates.Inputs;

public class IncompleteExternalCallOutputException extends ExceptionDecorator {

	private static final long serialVersionUID = 1L;
	
	private final String message;
	
	public enum Process {
		BUILDING_URL("Problem occurred while building URL."),
		BUILDING_HEADERS("Problem occurred while building headers for request."),
		BUILDING_REQUEST("Problem occurred while building request with inputs."),
		CALLING_SERVICE("Problem occurred while calling service."),
		MAPPING_RESPONSE_TO_RESPONSESTATUS("Problem occurred while mapping response to response status."),
		MAPPING_RESPONSE_TO_MODEL("Problem occurred while mapping response with model."),
		
		EXECUTING_PROCEDURE(""), EXECUTING_SQLQUERY(""), MAPPING_OUTPUT_TO_RESPONSESTATUS(""),
		MAPPING_OUTPUT_TO_RESPONSE("");
		
		private final String processErrorMessage;
		
		Process(String processErrorMessage) {
			this.processErrorMessage = processErrorMessage;
		}
		
		public String getProcessErrorMessage() {
			return this.processErrorMessage;
		}
	}
	
	private final String externalAPIURL;
	private final Inputs inputs;
	private final Exception exception;
	private final Process process;
	
	public static class Builder {
		private String forExternalAPIURL;
		private Inputs withInputs;
		private Exception exception;
		private Process occurredWhile;
		
		public Builder(Exception exception) {
			this.exception = exception;
		}
		
		public Exception getException() {
			return exception;
		}
		
		public Builder withInputs(Inputs withInputs) {
			this.withInputs = withInputs;
			return this;
		}
		
		public Inputs getWithInputs() {
			return withInputs;
		}
		
		public Builder forExternalAPIURL(String forExternalAPIURL) {
			this.forExternalAPIURL = forExternalAPIURL;
			return this;
		}
		
		public String getForExternalAPIURL() {
			return forExternalAPIURL;
		}
		
		public Process getOccurredWhile() {
			return occurredWhile;
		}
		
		public Builder occurredWhile(Process occurredWhile) {
			this.occurredWhile = occurredWhile;
			return this;
		}
		
		public IncompleteExternalCallOutputException build() {
			return new IncompleteExternalCallOutputException(this);
		}
	}
	
	public IncompleteExternalCallOutputException(Builder builder) {
		externalAPIURL = builder.getForExternalAPIURL();
		inputs = builder.getWithInputs();
		exception = builder.getException();
		process = builder.getOccurredWhile();
		StringBuilder sb = new StringBuilder();
		sb.append(process.getProcessErrorMessage()).append(" using ").append(externalAPIURL).append(" with inputs ").append(inputs.toString()).append(".Root cause is : ").append(exception.getMessage());
		message = sb.toString();
	}
	
	@Override
	public String getMessage() {
		return message;
	}
	
	public String externalAPIURL() {
		return externalAPIURL;
	}
	
	public Inputs inputs() {
		return inputs;
	}

	public Exception exception() {
		return exception;
	}
	
	public Process process() {
		return process;
	}
}
