package com.ftec.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import com.ftec.configs.enums.TutorialSteps;

@Document(indexName = "users")
public class User {
	
    @Id
    private long id;

    private String username;

    private String password;

    private String email;
    
    private TutorialSteps currentStep;
    
    private boolean subscribeForNews;

    public User() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isSubscribeForNews() {
        return subscribeForNews;
    }

    public void setSubscribeForNews(boolean subscribeForNews) {
        this.subscribeForNews = subscribeForNews;
    }

	public TutorialSteps getStep() {
		return currentStep;
	}

	public void setCurrentStep(TutorialSteps step) {
		this.currentStep = step;
	}
}
