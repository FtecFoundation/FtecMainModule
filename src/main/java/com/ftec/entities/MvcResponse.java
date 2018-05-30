package com.ftec.entities;

import org.springframework.http.HttpStatus;

public class MvcResponse<T> {
    private HttpStatus status;
    private T response;
    private String error;

    public MvcResponse() {
    }

    public MvcResponse(HttpStatus status, T response, String error) {
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

    public T getResponse() {
        return response;
    }

    public void setResponse(T response) {
        this.response = response;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
