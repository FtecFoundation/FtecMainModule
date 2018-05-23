package com.ftec.entities;

import com.ftec.controllers.RegistrationController;
import com.ftec.resources.Resources;
import com.ftec.resources.Stocks;
import com.ftec.resources.TutorialStep;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.context.i18n.LocaleContextHolder;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Random;

@Entity
public class User {
    //TODO check constraints; Change to NoSQL document
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;
    @NotNull
    @Size(min=3, max=50, message="{user.login.size}")
    @NotEmpty(message="{user.login.not_empty}")
    private String login;
    @NotNull
    @Size(min=6, message="{user.password.size}")
    @NotEmpty(message="{user.password.not_empty}")
    private String password;
    @NotNull
    @Pattern(message="{user.email.valid}",regexp="(?:[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-zA-Z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?\\.)+[a-zA-Z0-9](?:[a-zA-Z0-9-]*[a-zA-Z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-zA-Z0-9-]*[a-zA-Z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])")
    @NotEmpty(message="{user.email.not_empty}")
    private String email;
    private double balance;
    private String googleSecret;
    private TutorialStep tutorialStep;
    private String lang;
    private String passToken;
    private Date passTokenExpirationDate;
    private boolean qrEnabled;
    private String roles;
    private double pendingPartnerBalance;
    private double withdrawPartnerBalance;
    private Stocks currentStock;
    @CreationTimestamp
    @Temporal(TemporalType.DATE)
    private Date created;
    private boolean trial;
    private boolean banned;
    private boolean subscribedToEmail;
    private int paymentsMade;
    @OneToOne(cascade = CascadeType.PERSIST)
    private Payment payment;

    public User() {
    }

    public User(RegistrationController.RegistrationUser user){
        this.login = user.getUsername();
        this.email = user.getEmail();
        this.googleSecret = getRandomGoogleSecret();
        this.balance= Resources.startingBalance;
        this.tutorialStep=TutorialStep.Step1;
        this.lang = LocaleContextHolder.getLocale().getLanguage();
        this.passToken=Resources.disabledPassword;
        this.qrEnabled=false;
        this.roles="ROLE_USER";
        this.trial=true;
        this.paymentsMade=0;
        this.subscribedToEmail=user.isSubscribedToEmail();
        this.currentStock=Stocks.Poloniex;
        this.pendingPartnerBalance=0;
        this.withdrawPartnerBalance=0;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
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

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public String getGoogleSecret() {
        return googleSecret;
    }

    public void setGoogleSecret(String googleSecret) {
        this.googleSecret = googleSecret;
    }

    public TutorialStep getTutorialStep() {
        return tutorialStep;
    }

    public void setTutorialStep(TutorialStep tutorialStep) {
        this.tutorialStep = tutorialStep;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getPassToken() {
        return passToken;
    }

    public void setPassToken(String passToken) {
        this.passToken = passToken;
    }

    public Date getPassTokenExpirationDate() {
        return passTokenExpirationDate;
    }

    public void setPassTokenExpirationDate(Date passTokenExpirationDate) {
        this.passTokenExpirationDate = passTokenExpirationDate;
    }

    public boolean isQrEnabled() {
        return qrEnabled;
    }

    public void setQrEnabled(boolean qrEnabled) {
        this.qrEnabled = qrEnabled;
    }

    public String getRoles() {
        return roles;
    }

    public void setRoles(String roles) {
        this.roles = roles;
    }

    public double getPendingPartnerBalance() {
        return pendingPartnerBalance;
    }

    public void setPendingPartnerBalance(double pendingPartnerBalance) {
        this.pendingPartnerBalance = pendingPartnerBalance;
    }

    public double getWithdrawPartnerBalance() {
        return withdrawPartnerBalance;
    }

    public void setWithdrawPartnerBalance(double withdrawPartnerBalance) {
        this.withdrawPartnerBalance = withdrawPartnerBalance;
    }

    public Stocks getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(Stocks currentStock) {
        this.currentStock = currentStock;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public boolean isTrial() {
        return trial;
    }

    public void setTrial(boolean trial) {
        this.trial = trial;
    }

    public boolean isBanned() {
        return banned;
    }

    public void setBanned(boolean banned) {
        this.banned = banned;
    }

    public boolean isSubscribedToEmail() {
        return subscribedToEmail;
    }

    public void setSubscribedToEmail(boolean subscribedToEmail) {
        this.subscribedToEmail = subscribedToEmail;
    }

    public int getPaymentsMade() {
        return paymentsMade;
    }

    public void setPaymentsMade(int paymentsMade) {
        this.paymentsMade = paymentsMade;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public void addToBalance(double amount) {
        this.balance += amount;
    }

    private String getRandomGoogleSecret(){
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 18) { // length of the random string.
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }
}
