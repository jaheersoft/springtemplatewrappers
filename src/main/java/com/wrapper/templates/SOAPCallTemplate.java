package com.wrapper.templates;

import java.util.Map;
import org.springframework.util.MultiValueMap;
import com.wrapper.templates.callers.SOAPServiceCaller;
import com.wrapper.templates.exceptions.FaultException;

public abstract class SOAPCallTemplate<REQUEST,RESPONSE> {

	public SOAPCallTemplate(SOAPServiceCaller caller) {
		this.caller = caller;
	}
	
	private SOAPServiceCaller caller;
	
	private REQUEST request;
	
	private String externalAPIURI;
	
	private String wsdlPackageName;
	
	private Map<String,?> requestInputs;
	
	private Map<String,String> inputHeaders;
	
	private Map<String,String> pathVariables;
	
	private Map<String,String> inputParams;
	
	private RESPONSE response;
	
	private String url;
	
	private MultiValueMap<String,String> headers;
	
	public SOAPCallTemplate<REQUEST,RESPONSE> wsdlPackageName(String wsdlPackageName) {
		this.wsdlPackageName = wsdlPackageName;
		return this;
	}
	
	public SOAPCallTemplate<REQUEST,RESPONSE> withExternalAPIURI(String externalAPIURI) {
		this.externalAPIURI = externalAPIURI;
		return this;
	}

	public SOAPCallTemplate<REQUEST,RESPONSE> withRequestInputs(Map<String,?> requestInputs) {
		this.requestInputs = requestInputs;
		return this;
	}
	
	public SOAPCallTemplate<REQUEST,RESPONSE> withPathVariables(Map<String,String> pathVariables) {
		this.pathVariables = pathVariables;
		return this;
	}
	
	public SOAPCallTemplate<REQUEST,RESPONSE> withInputParams(Map<String,String> inputParams) {
		this.inputParams = inputParams;
		return this;
	}
	
	public SOAPCallTemplate<REQUEST,RESPONSE> withInputHeaders(Map<String,String> inputHeaders) {
		this.inputHeaders = inputHeaders;
		return this;
	}
	
	public SOAPCallTemplate<REQUEST,RESPONSE> call() throws FaultException {
		this.url = resourceURL(externalAPIURI(), pathVariables(), inputParams());
		this.headers = headers(inputHeaders());
		this.request = buildPayload(requestInputs());
		this.response = caller().callService(url(), wsdlPackageName(), request());
		return this;
	}
	
	public REQUEST request() {
		return request;
	}
	
	public Map<String,?> requestInputs() {
		return requestInputs;
	}

	public RESPONSE response() {
		return response;
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

	public String wsdlPackageName() {
		return wsdlPackageName;
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

	public SOAPServiceCaller caller() {
		return caller;
	}
	
	protected abstract REQUEST buildPayload(Map<String,?> requestInputs);
	protected abstract String resourceURL(String externalAPIURI,Map<String,String> pathVariables,Map<String,String> inputParams);
	protected abstract MultiValueMap<String,String> headers(Map<String,String> headers);
}
