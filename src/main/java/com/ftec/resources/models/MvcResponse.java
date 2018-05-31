package com.ftec.resources.models;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.ANY, getterVisibility=JsonAutoDetect.Visibility.NONE,
        setterVisibility=JsonAutoDetect.Visibility.NONE, creatorVisibility=JsonAutoDetect.Visibility.NONE)
public class MvcResponse {
    @JsonProperty("status")
    private int status;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("response")
    private HashMap<String, Object> response;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("error")
    private String error;

    public MvcResponse() {
    	response = new HashMap<>();
    }

    public MvcResponse(int status, String paramName, Object object){
        this.status = status;
        response = new HashMap<>();
        response.put(paramName, object);
    }

    public MvcResponse(int status, Object... objects){
        this.status = status;
        response = new HashMap<>();
        for(Object object: objects){
            response.put(object.getClass().getName(), object);
        }
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public void addParam(Object object){
        response.put(object.getClass().getName(), object);
    }
    public void addParam(String paramName, Object object){
        response.put(object.getClass().getName(), object);
    }
    public void addParam(Object... objects){
        for(Object object: objects){
            response.put(object.getClass().getName(), object);
        }
    }

    public Map<String, Object> getParams(){
        return response;
    }
    public void clearParams(){
        response = new HashMap<>();
    }
	@Override
	public String toString() {
		return "MvcResponse [status=" + status + ", response=" + response + ", error=" + error + "]";
	}
}
