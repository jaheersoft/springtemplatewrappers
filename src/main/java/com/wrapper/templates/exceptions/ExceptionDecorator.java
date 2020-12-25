package com.wrapper.templates.exceptions;

@SuppressWarnings("serial")
public abstract class ExceptionDecorator extends Exception {

	public abstract String getMessage();
}
