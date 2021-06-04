package com.wrapper.templates.clients;

import java.util.Map;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import com.wrapper.templates.Inputs;
import com.wrapper.templates.callers.SOAPServiceCaller;
import com.wrapper.templates.exceptions.IncompleteExternalCallOutputException;
import com.wrapper.templates.exceptions.IncompleteExternalCallOutputException.Process;

public abstract class AbstractSOAPClient<R> {

	private SOAPServiceCaller caller;

	private String externalAPIURI;

	private String wsdlPackageName;

	private Inputs inputs;

	private Class<R> responseClass;

	private Jaxb2Marshaller marshaller;

	public AbstractSOAPClient(SOAPServiceCaller caller) {
		this.caller = caller;
	}

	public SOAPServiceCaller caller() {
		return caller;
	}

	public AbstractSOAPClient<R> withExternalAPIURI(String externalAPIURI) {
		this.externalAPIURI = externalAPIURI;
		return this;
	}

	public String externalAPIURI() {
		return externalAPIURI;
	}

	public AbstractSOAPClient<R> from(Inputs inputs) {
		this.inputs = inputs;
		return this;
	}

	public Inputs inputs() {
		return inputs;
	}

	public AbstractSOAPClient<R> withWSDLPackageName(String wsdlPackageName) {
		this.wsdlPackageName = wsdlPackageName;
		return this;
	}

	public String wsdlPackageName() {
		return wsdlPackageName;
	}

	public AbstractSOAPClient<R> forResponseClass(Class<R> responseClass) {
		this.responseClass = responseClass;
		return this;
	}

	public Class<R> responseClass() {
		return responseClass;
	}

	public AbstractSOAPClient<R> withJaxbMarshaller(Jaxb2Marshaller marshaller) {
		this.marshaller = marshaller;
		return this;
	}

	public Jaxb2Marshaller marshaller() {
		return marshaller;
	}

	private String url;

	public String url() {
		return url;
	}

	private R response;

	public R response() {
		return response;
	}

	private Map<String, ?> headers;

	public Map<String, ?> headers() {
		return headers;
	}

	public <Q> AbstractSOAPClient<R> call() throws IncompleteExternalCallOutputException {
		Q request = null;
		try {
			this.url = resourceURL(externalAPIURI(), inputs().getPayloads());
		} catch (Exception e) {
			throw new IncompleteExternalCallOutputException.Builder(e).occurredWhile(Process.BUILDING_URL)
					.forExternalAPIURL(externalAPIURI()).withInputs(inputs()).build();
		}
		try {
			request = buildPayload(inputs().getPayloads());
		} catch (Exception e) {
			throw new IncompleteExternalCallOutputException.Builder(e).occurredWhile(Process.BUILDING_REQUEST)
					.forExternalAPIURL(externalAPIURI()).withInputs(inputs()).build();
		}
		try {
			headers = headers(inputs().getHeaders());
		} catch (Exception e) {
			throw new IncompleteExternalCallOutputException.Builder(e).occurredWhile(Process.BUILDING_HEADERS)
					.forExternalAPIURL(externalAPIURI()).withInputs(inputs()).build();
		}
		try {
			this.response = caller().callService(url(), marshaller(), headers(), request);
		} catch (Exception e) {
			throw new IncompleteExternalCallOutputException.Builder(e).occurredWhile(Process.CALLING_SERVICE)
					.forExternalAPIURL(externalAPIURI()).withInputs(inputs()).build();
		}
		return this;
	}

	protected abstract <Q> Q buildPayload(Map<String, ?> payloads);

	protected abstract String resourceURL(String externalAPI, Map<String, ?> inputParams);

	protected abstract Map<String, ?> headers(Map<String, ?> headers);
}
