package com.ftec.entities;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;

public class MvcResponse <T> {
    private HttpStatus status;
    private HashMap<String,T> response;
    private String error;

    public MvcResponse() {
    	response = new HashMap<String,T>();
    }

    public MvcResponse(HttpStatus status, HashMap<String,T> response, String error) {
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

	public Map<String, T> getResponse() {
		return response;
	}

	public void setResponse(HashMap<String, T> response) {
		this.response = response;
	}
}
