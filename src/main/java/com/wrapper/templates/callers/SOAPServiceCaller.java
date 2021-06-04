package com.wrapper.templates.callers;

import java.util.Map;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import com.wrapper.templates.exceptions.FaultError;
import com.wrapper.templates.exceptions.FaultError.ErrorType;

@Component
public class SOAPServiceCaller extends WebServiceGatewaySupport {

	@SuppressWarnings("unchecked")
	public <REQUEST, RESPONSE> RESPONSE callService(String endPointURL, Jaxb2Marshaller marshaller,
			Map<String, ?> headers, REQUEST request) throws FaultError {
		RESPONSE response = null;
		try {
			getWebServiceTemplate().setMarshaller(marshaller);
			getWebServiceTemplate().setUnmarshaller(marshaller);
			response = (RESPONSE) getWebServiceTemplate().marshalSendAndReceive(endPointURL, request);
		} catch (Exception e) {
			throw new FaultError.Builder(e).theError(ErrorType.SOAP_ERROR).withOccurredWhileCalling(endPointURL).build();
		} finally {

		}
		if (response == null) {
			throw new FaultError.Builder("Null response").theError(ErrorType.SOAP_ERROR).withOccurredWhileCalling(endPointURL).build();
		}
		return response;
	}
}
