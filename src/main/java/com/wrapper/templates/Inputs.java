package com.wrapper.templates;

import java.util.HashMap;
import java.util.Map;

public final class Inputs {
	
	private Map<String,String> headers;
	
	private Map<String, Object> payloads = new HashMap<>();
	
	public Inputs() {}
	
	public Inputs(Map<String,String> headers) {
		this.headers = headers;
	}
	
	public Map<String,String> getHeaders() {
		return headers;
	}
	
	public Map<String,Object> getPayloads() {
		return payloads;
	}
}
