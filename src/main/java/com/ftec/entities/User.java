package com.ftec.entities;

import com.ftec.configs.enums.TutorialSteps;
import com.ftec.controllers.ChangeSettingController.UserUpdate;
import com.ftec.controllers.RegistrationController;
import com.ftec.utils.PasswordUtils;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@Entity
@Table
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    protected long id;

    private String username;

    private String password;

    private String email;

    private TutorialSteps currentStep;

    private boolean subscribeForNews;

    private String salt;

    private Boolean twoStepVerification;

    public User(@NotNull @Size(min = 3) String username, @NotNull @Size(min = 4) String password, @NotNull String email, TutorialSteps currentStep, @NotNull boolean subscribeForNews, @NotNull Boolean twoStepVerification) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.currentStep = currentStep;
        this.subscribeForNews = subscribeForNews;
        this.twoStepVerification = twoStepVerification;
    }

    public User() {
    }

    public User(RegistrationController.UserRegistration userRegistration) {
        this.username = userRegistration.getUsername();
        this.password = userRegistration.getPassword();
        this.email = userRegistration.getEmail();
        this.subscribeForNews = userRegistration.isSubscribeForNews();
    }

    public void apllyChangeSettings(UserUpdate userUpdate) {
        if(userUpdate.getTwoFactorEnabled() != null) this.twoStepVerification = userUpdate.getTwoFactorEnabled();
        if(userUpdate.getPassword() != null) this.password = userUpdate.getPassword();
        if(userUpdate.getEmail() != null) this.email = userUpdate.getEmail();
        if(userUpdate.getSubscribeForNews() != null) this.subscribeForNews = userUpdate.getSubscribeForNews();

    }

    public void fillEmptyFields() {
        this.currentStep = TutorialSteps.FIRST;
        this.twoStepVerification = false;
        this.salt = PasswordUtils.getSalt(10);
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", username=" + username + ", password=" + password + ", email=" + email + ", currentStep=" + currentStep + ", subscribeForNews=" + subscribeForNews + "]";
    }
}
