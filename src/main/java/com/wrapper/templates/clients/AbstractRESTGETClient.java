package com.wrapper.templates.clients;

import java.util.Map;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MultiValueMap;
import com.wrapper.templates.Inputs;
import com.wrapper.templates.callers.RESTServiceCaller;
import com.wrapper.templates.exceptions.ApplicationException;
import static com.wrapper.templates.exceptions.ErrorType.*;
import com.wrapper.templates.exceptions.FaultError;
import static com.wrapper.templates.exceptions.Process.*;

public abstract class AbstractRESTGETClient<R> {

	private RESTServiceCaller caller;
	
	private String externalAPIURI;
	
	private Inputs inputs;
	
	private Class<R> responseClass;
	
	public AbstractRESTGETClient(RESTServiceCaller caller) {
		this.caller = caller;
	}
	
	public AbstractRESTGETClient<R> withExternalAPIURI(String externalAPIURI) {
		this.externalAPIURI = externalAPIURI;
		return this;
	}
	
	public AbstractRESTGETClient<R> from(Inputs inputs) {
		this.inputs = inputs;
		return this;
	}
	
	public AbstractRESTGETClient<R> forResponseClass(Class<R> responseClass) {
		this.responseClass = responseClass;
		return this;
	}
	
	public Inputs inputs() {
		return inputs;
	}
	
	public RESTServiceCaller caller() {
		return caller;
	}
	
	public String externalAPIURI() {
		return externalAPIURI;
	}
	
	public Class<R> responseClass() {
		return responseClass;
	}
	
	private String url;
	
	private HttpEntity<?> httpEntity;
	
	private ResponseEntity<R> responseEntity;
	
	private R response;
	
	private MultiValueMap<String,String> headers;
	
	public String url() {
		return url;
	}
	
	public HttpEntity<?> httpEntity() {
		return httpEntity;
	}
	
	public ResponseEntity<R> responseEntity() {
		return responseEntity;
	}
	
	public R response() {
		return response;
	}
	
	private MultiValueMap<String,String> headers() {
		return headers;
	}
	
	public AbstractRESTGETClient<R> call() throws ApplicationException,FaultError {
		try {
			this.url = resourceURL(externalAPIURI(),inputs().getPayloads());
		} catch(Exception e) {
			throw new ApplicationException.Builder(e).the(REST_ERROR).occurredWhile(BUILDING_URL).build();
		}
		try {
			this.headers = headers(inputs().getHeaders());
		} catch(Exception e) {
			throw new ApplicationException.Builder(e).the(REST_ERROR).occurredWhile(BUILDING_HEADERS).build();
		}
		this.httpEntity = new HttpEntity<>(null,headers());
		this.responseEntity = caller().callOperationAndReturnEntity(url(),HttpMethod.GET,httpEntity(),responseClass());
		return this;
	}
	
	protected abstract String resourceURL(String externalAPI,Map<String,?> payloads);
	
	protected abstract MultiValueMap<String,String> headers(Map<String,String> inputHeaders);
}
