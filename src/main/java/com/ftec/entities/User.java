package com.ftec.entities;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

import com.ftec.configs.enums.TutorialSteps;
import com.ftec.controllers.ChangeSettingController.UserUpdate;
import com.ftec.controllers.RegistrationController;

@Document(indexName = "#{@users}")
public class User {

    @Id
    private long id;

    @NotNull
    @Size(min = 3)
    private String username;

    @NotNull
    @Size(min = 4)
    private String password;

    @NotNull
    private String email;

    private TutorialSteps currentStep;

    @NotNull
    private boolean subscribeForNews;

    @NotNull
    private Boolean twoStepVerification;


    public User() {
    }

    public User(RegistrationController.UserRegistration userRegistration) {
        this.username = userRegistration.getUsername();
        this.password = userRegistration.getPassword();
        this.email = userRegistration.getEmail();
        this.subscribeForNews = userRegistration.isSubscribeForNews();
    }

    public void apllyChangeSettings(UserUpdate userUpdate) {
    	changeEmailIfRequired(userUpdate.getEmail());
		changePasswordIfRequired(userUpdate.getPassword());
		changeTwoFactorIfRequired(userUpdate.isTwoFactorEnabled());
		changeSubscribeForNewsIfRequired(userUpdate.isTwoFactorEnabled());
    }
    
    private void changeTwoFactorIfRequired(Boolean twoStepVerification) {
		if(twoStepVerification != null) this.twoStepVerification = twoStepVerification;
	}

	private void changePasswordIfRequired(String password) {
    	if(password != null) this.password = password;
	}

	private void changeEmailIfRequired(String email) {
		if(email != null) this.email = email;
	}
    
	private void changeSubscribeForNewsIfRequired(Boolean subscribeForNews) {
		if(subscribeForNews != null) this.subscribeForNews = subscribeForNews;
	}
	
    public void fillEmptyFields() {
        //todo add another fields
        this.currentStep = TutorialSteps.FIRST;
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

    public TutorialSteps getCurrentStep() {
        return currentStep;
    }

    public void setCurrentStep(TutorialSteps currentStep) {
        this.currentStep = currentStep;
    }

    public Boolean isTwoStepVerification() {
        return twoStepVerification;
    }

    public void setTwoStepVerification(Boolean twoStepVerification) {
        this.twoStepVerification = twoStepVerification;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", username=" + username + ", password=" + password + ", email=" + email + ", currentStep=" + currentStep + ", subscribeForNews=" + subscribeForNews + "]";
    }
}
