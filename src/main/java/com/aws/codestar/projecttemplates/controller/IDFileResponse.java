package com.aws.codestar.projecttemplates.controller;

import java.util.HashMap;

public class IDFileResponse {
	
	private HashMap fields ;
	
	public IDFileResponse() {
		fields = new HashMap();
	}

	public HashMap getFields() {
		return fields;
	}

	public void setFields(HashMap values) {
		this.fields = values;
	}

}
