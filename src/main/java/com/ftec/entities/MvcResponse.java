package com.ftec.entities;

import org.apache.http.HttpResponse;
import org.springframework.http.HttpStatus;

public abstract class MvcResponse {
    private HttpStatus status;
    private HttpResponse response;
    private String error;

    public MvcResponse() {
    }

    public MvcResponse(HttpStatus status, HttpResponse response, String error) {
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

    public HttpResponse getResponse() {
        return response;
    }

    public void setResponse(HttpResponse response) {
        this.response = response;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
