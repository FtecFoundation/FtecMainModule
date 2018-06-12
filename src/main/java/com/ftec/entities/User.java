package com.ftec.entities;

import com.ftec.controllers.ChangeSettingController.UserUpdate;
import com.ftec.controllers.RegistrationController;
import com.ftec.resources.enums.TutorialSteps;
import com.ftec.resources.enums.UserRole;
import com.ftec.utils.PasswordUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Locale;

@Data
@AllArgsConstructor
@Entity
@Table
@ToString
public class User {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO, generator="native")
    @GenericGenerator(name = "native", strategy = "native")
    protected long id;

    private String username;

    private String password;

    private String email;

    private TutorialSteps currentStep;

    private boolean subscribeForEmail;

    private String salt;

    private Boolean twoStepVerification;

    private UserRole userRole;

    private boolean banned;

    private Locale locale;

    private String imageName;

    public User() {
    }

    public User(@NotNull @Size(min = 3) String username, @NotNull @Size(min = 4) String password, @NotNull String email, TutorialSteps currentStep, @NotNull boolean subscribeForEmail, @NotNull Boolean twoStepVerification) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.currentStep = currentStep;
        this.subscribeForEmail = subscribeForEmail;
        this.twoStepVerification = twoStepVerification;
    }


    public User(RegistrationController.UserRegistration userRegistration) {
        this.username = userRegistration.getUsername();
        this.password = userRegistration.getPassword();
        this.email = userRegistration.getEmail();
        this.subscribeForEmail = userRegistration.isSubscribeForEmail();
    }

    public void applyChangeSettings(UserUpdate userUpdate) {
        if (userUpdate.getTwoFactorEnabled() != null) this.twoStepVerification = userUpdate.getTwoFactorEnabled();
        if (userUpdate.getPassword() != null) changePassword(userUpdate);

        if (userUpdate.getEmail() != null) this.email = userUpdate.getEmail();
        if (userUpdate.getSubscribeForEmail() != null) this.subscribeForEmail = userUpdate.getSubscribeForEmail();
    }

    private void changePassword(UserUpdate userUpdate) {

        this.password = PasswordUtils.generateSecurePassword(userUpdate.getPassword(), this.salt);
    }

    public void fillEmptyFields() {
        this.currentStep = TutorialSteps.FIRST;
        this.salt = PasswordUtils.getSalt(10);
        this.banned = false;

        if (this.userRole == null) this.twoStepVerification = false;
        if (this.userRole == null) this.userRole = UserRole.USER;
        if (locale == null) locale = new Locale("en");
    }


}
