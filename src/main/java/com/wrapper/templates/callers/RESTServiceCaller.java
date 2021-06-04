package com.wrapper.templates.callers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.wrapper.templates.exceptions.FaultError;
import com.wrapper.templates.exceptions.FaultError.ErrorType;

@Component
public class RESTServiceCaller {

	@Autowired
	private RestTemplate template;

	public <RESPONSE> ResponseEntity<RESPONSE> callOperationAndReturnEntity(String serviceCallURL,
			HttpMethod httpMethod, HttpEntity<?> httpEntity, Class<RESPONSE> responseClass) throws FaultError {
		ResponseEntity<RESPONSE> responseEntity = null;
		try {
			responseEntity = this.template.exchange(serviceCallURL, httpMethod, httpEntity, responseClass);
		} catch (Exception e) {
			throw new FaultError.Builder(e).theError(ErrorType.REST_ERROR).withOccurredWhileCalling(serviceCallURL).build();
		} finally {

		}
		if (responseEntity == null) {
			throw new FaultError.Builder("Null response").theError(ErrorType.REST_ERROR).withOccurredWhileCalling(serviceCallURL).build();
		}
		return responseEntity;
	}

	public <RESPONSE> RESPONSE callOperationAndGetResponse(String serviceCallURL, HttpMethod method,
			HttpEntity<?> entity, Class<RESPONSE> responseClass) throws FaultError {
		return callOperationAndReturnEntity(serviceCallURL, method, entity, responseClass).getBody();
	}
}
