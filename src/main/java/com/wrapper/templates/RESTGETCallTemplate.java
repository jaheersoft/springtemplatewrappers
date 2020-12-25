package com.wrapper.templates;

import java.util.Map;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import com.wrapper.templates.callers.RESTServiceCaller;
import com.wrapper.templates.exceptions.FaultException;

public abstract class RESTGETCallTemplate<RESPONSE> {
	
	public RESTGETCallTemplate(RESTServiceCaller caller) {
		this.caller = caller;
	}
	
	private HttpEntity<?> httpEntity;
	
	private ResponseEntity<RESPONSE> responseEntity;
	
	private RESPONSE response;
	
	private Class<RESPONSE> responseClass;
	
	private String url;
	
	private MultiValueMap<String,String> headers;
	
	private String externalAPIURI;
	
	private Map<String,String> inputHeaders;
	
	private Map<String,String> pathVariables;
	
	private Map<String,String> inputParams;
	
	private RESTServiceCaller caller;
	
	public RESTGETCallTemplate<RESPONSE> withExternalAPIURI(String externalAPIURI) {
		this.externalAPIURI = externalAPIURI;
		return this;
	}

	public RESTGETCallTemplate<RESPONSE> withPathVariables(Map<String,String> pathVariables) {
		this.pathVariables = pathVariables;
		return this;
	}
	
	public RESTGETCallTemplate<RESPONSE> withInputParams(Map<String,String> inputParams) {
		this.inputParams = inputParams;
		return this;
	}
	
	public RESTGETCallTemplate<RESPONSE> withInputHeaders(Map<String,String> inputHeaders) {
		this.inputHeaders = inputHeaders;
		return this;
	}
	
	public RESTGETCallTemplate<RESPONSE> forResponseClass(Class<RESPONSE> responseClass) {
		this.responseClass = responseClass;
		return this;
	}
	
	public RESTGETCallTemplate<RESPONSE> call() throws FaultException {
		this.url = resourceURL(externalAPIURI(), pathVariables(), inputParams());
		this.headers = headers(inputHeaders());
		this.httpEntity = new HttpEntity<>(null,headers());
		this.responseEntity = caller().callOperationAndReturnEntity(url(), HttpMethod.GET, httpEntity(), responseClass());
		this.response = responseEntity().getBody();
		return this;
	}
	
	public HttpEntity<?> httpEntity() {
		return httpEntity;
	}

	public ResponseEntity<RESPONSE> responseEntity() {
		return responseEntity;
	}

	public RESPONSE response() {
		return response;
	}

	public Class<RESPONSE> responseClass() {
		return responseClass;
	}

	public String url() {
		return url;
	}

	public MultiValueMap<String, String> headers() {
		return headers;
	}

	public String externalAPIURI() {
		return externalAPIURI;
	}

	public Map<String, String> inputHeaders() {
		return inputHeaders;
	}
	
	public Map<String, String> pathVariables() {
		return pathVariables;
	}
	
	public Map<String, String> inputParams() {
		return inputParams;
	}

	public RESTServiceCaller caller() {
		return caller;
	}
	
	protected abstract String resourceURL(String externalAPIURI,Map<String,String> pathVariables,Map<String,String> inputParams);
	protected abstract MultiValueMap<String,String> headers(Map<String,String> headers);
}
