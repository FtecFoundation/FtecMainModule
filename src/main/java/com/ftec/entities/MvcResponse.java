package com.ftec.entities;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;

public class MvcResponse {
    private HttpStatus status;
    private Map<String,User> response;
    private String error;

    public MvcResponse() {
    	response = new HashMap<String,User>();
    }

    public MvcResponse(HttpStatus status, Map<String,User> response, String error) {
        this.status = status;
        this.response = response;
        this.error = error;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

	@Override
	public String toString() {
		return "MvcResponse [status=" + status + ", response=" + response + ", error=" + error + "]";
	}

	public Map<String, User> getResponse() {
		return response;
	}

	public void setResponse(Map<String, User> response) {
		this.response = response;
	}
}
