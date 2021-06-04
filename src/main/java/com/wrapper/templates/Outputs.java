package com.wrapper.templates;

import java.util.List;
import org.springframework.http.ResponseEntity;
import com.wrapper.templates.models.ResponseStatus;

public final class Outputs<R> {
	
	private ResponseStatus responseStatus;
	
	private R response;
	
	private List<R> responses;
	
	private ResponseEntity<R> responseEntity;
	
	//private StoredProcedureResults<R> storedProcedureResponses;
	
	public Outputs(ResponseEntity<R> responseEntity,ResponseStatus responseStatus) {
		this.responseEntity = responseEntity;
		this.responseStatus = responseStatus;
	}

	public Outputs(R response,ResponseStatus responseStatus) {
		this.response = response;
		this.responseStatus = responseStatus;
	}
	
	/*public Outputs(StoredProcedureResults<R> storedProcedureResponses,ResponseStatus responseStatus) {
		this.storedProcedureResponses = storedProcedureResponses;
		this.responseStatus = responseStatus;
	}*/
	
	public Outputs(List<R> responses,ResponseStatus responseStatus) {
		this.responses = responses;
		this.responseStatus = responseStatus;
	}
	
	public ResponseStatus responseStatus() {
		return responseStatus;
	}
	
	public ResponseEntity<R> responseEntity() {
		return responseEntity;
	}
	
	public R response() {
		return response;
	}
	
	public List<R> responses() {
		return responses;
	}
	
	/*public StoredProcedureResults<R> storedProcedureResponses() {
		return storedProcedureResponses;
	}*/
}
