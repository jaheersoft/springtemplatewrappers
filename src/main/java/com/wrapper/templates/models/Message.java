package com.wrapper.templates.models;

import java.util.List;

public final class Message {
	
	private String code;
	private String text;
	private String type;
	private String description;
	private List<String> parameters;
	
	public Message code(String code) {
		this.code = code;
		return this;
	}
	
	public Message parameters(List<String> parameters) {
		this.parameters = parameters;
		return this;
	}
	
	public Message type(String type) {
		this.type = type;
		return this;
	}
	
	public Message description(String description) {
		this.description = description;
		return this;
	}
	
	public Message text(String text) {
		this.text = text;
		return this;
	}

	public String code() {
		return this.code;
	}
	
	public String text() {
		return this.text;
	}
	
	public String type() {
		return this.type;
	}
	
	public String description() {
		return this.description;
	}
	
	public List<String> parameters() {
		return this.parameters;
	}
}
