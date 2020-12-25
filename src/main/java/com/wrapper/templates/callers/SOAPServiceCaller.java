package com.wrapper.templates.callers;

import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Component;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import com.wrapper.templates.exceptions.FaultException;
import com.wrapper.templates.exceptions.FaultException.ErrorType;

@Component
public class SOAPServiceCaller extends WebServiceGatewaySupport {

	@SuppressWarnings("unchecked")
	public <REQUEST, RESPONSE> RESPONSE callService(String endPointURL, String wsdlName, REQUEST request)
			throws FaultException {
		RESPONSE response = null;
		try {
			final Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
			marshaller.setContextPath(wsdlName);
			getWebServiceTemplate().setMarshaller(marshaller);
			getWebServiceTemplate().setUnmarshaller(marshaller);
			response = (RESPONSE) getWebServiceTemplate().marshalSendAndReceive(endPointURL, request);
		} catch (Exception e) {
			throw new FaultException.Builder(e).consumerInputs(wsdlName).withServiceCalledURL(endPointURL)
					.forErrorType(ErrorType.SOAP_ERROR).build();
		} finally {

		}
		if (response == null) {
			throw new FaultException.Builder("").consumerInputs(wsdlName).withServiceCalledURL(endPointURL)
					.forErrorType(ErrorType.SOAP_ERROR).build();
		}
		return response;
	}
}
