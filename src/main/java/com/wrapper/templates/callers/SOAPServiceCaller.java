package com.wrapper.templates.callers;

import java.util.Map;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import static com.wrapper.templates.exceptions.ErrorType.*;
import com.wrapper.templates.exceptions.FaultError;
import static com.wrapper.templates.exceptions.Process.*;

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
			throw new FaultError.Builder(e).the(SOAP_ERROR).occurredWhile(CALLING_SERVICE).build();
		} finally {

		}
		if (response == null) {
			throw new FaultError.Builder("Null response").the(SOAP_ERROR).occurredWhile(CALLING_SERVICE).build();
		}
		return response;
	}
}
