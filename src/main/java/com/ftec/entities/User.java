package com.ftec.entities;

import com.ftec.configs.enums.TutorialSteps;
import com.ftec.controllers.ChangeSettingController.UserUpdate;
import com.ftec.controllers.RegistrationController;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@Entity
@Table
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    protected long id;

    @NotNull
    @Size(min = 3)
    private String username;

    @NotNull
    @Size(min = 4)
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$")
    private String password;

    @NotNull
    private String email;

    private TutorialSteps currentStep;

    private boolean subscribeForNews;

    @NotNull
    private Boolean twoStepVerification;

    public User(@NotNull @Size(min = 3) String username, @NotNull @Size(min = 4) String password, @NotNull String email, TutorialSteps currentStep, @NotNull boolean subscribeForNews, @NotNull Boolean twoStepVerification) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.currentStep = currentStep;
        this.subscribeForNews = subscribeForNews;
        this.twoStepVerification = twoStepVerification;
    }

    public User(RegistrationController.UserRegistration userRegistration) {
        this.username = userRegistration.getUsername();
        this.password = userRegistration.getPassword();
        this.email = userRegistration.getEmail();
        this.subscribeForNews = userRegistration.isSubscribeForNews();
    }

    public User(){}

    public void apllyChangeSettings(UserUpdate userUpdate) {
    	changeEmailIfRequired(userUpdate.getEmail());
		changePasswordIfRequired(userUpdate.getPassword());
		changeTwoFactorIfRequired(userUpdate.getTwoFactorEnabled());
		changeSubscribeForNewsIfRequired(userUpdate.getSubscribeForNews());
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
        this.currentStep = TutorialSteps.FIRST;
        this.twoStepVerification = false;
    }

}
