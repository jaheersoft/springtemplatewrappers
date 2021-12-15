package com.wrapper.templates.callers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import static com.wrapper.templates.exceptions.ErrorType.*;
import com.wrapper.templates.exceptions.FaultError;
import static com.wrapper.templates.exceptions.Process.*;

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
			throw new FaultError.Builder(e).the(REST_ERROR).occurredWhile(CALLING_SERVICE).build();
		} finally {

		}
		if (responseEntity == null) {
			throw new FaultError.Builder("Null response").the(REST_ERROR).occurredWhile(CALLING_SERVICE).build();
		}
		return responseEntity;
	}

	public <RESPONSE> RESPONSE callOperationAndGetResponse(String serviceCallURL, HttpMethod method,
			HttpEntity<?> entity, Class<RESPONSE> responseClass) throws FaultError {
		return callOperationAndReturnEntity(serviceCallURL, method, entity, responseClass).getBody();
	}
}
