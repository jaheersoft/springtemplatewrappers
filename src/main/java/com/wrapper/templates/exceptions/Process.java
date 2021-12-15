package com.wrapper.templates.exceptions;

public enum Process {
	BUILDING_URL("building URL failed with exception :"),
	BUILDING_HEADERS("building request headers failed with exception :"),
	BUILDING_REQUEST("building request payload failed with exception :"),
	MAPPING_RESPONSE_TO_RESPONSESTATUS("mapping response to response status failed with exception :"),
	MAPPING_RESPONSE_TO_MODEL("mapping response with model failed with exception :"),
	BUILDING_PROCEDURE_PARAMETERS("building stored proc input,output parameters failed with exception :"),
	BUILDING_SQLQUERY_PARAMETERS("building sql query input,output parameters failed with exception :"), 
	MAPPING_OUTPUT_TO_RESPONSESTATUS("mapping output to response status failed with exception :"),
	MAPPING_OUTPUT_TO_RESPONSE("mapping output to model failed with exception :"),
	CALLING_SERVICE("hitting service failed with exception : "),
	EXECUTING_PROCEDURE("execution failed with exception : "),
	EXECUTING_SQLQUERY("execution failed with exception : "),
	POSTING_MESSAGE("message posting failed with exception : "),
	SAVING_OBJECT(""),
	UPDATING_OBJECT(""),
	FINDING_OBJECT(""),
	DELETING_OBJECT("");
	
	private final String processErrorMessage;

	Process(String processErrorMessage) {
		this.processErrorMessage = processErrorMessage;
	}

	public String getProcessErrorMessage() {
		return this.processErrorMessage;
	}
}
