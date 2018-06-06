package com.ftec.resources;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConfigurationProperties(prefix = "mail")
public class MailResources {

	public static String UserIdStatic;
	public static String userSecretStatic;
	public static String sendFromStatic;
	public static String sendToStatic;
	public static boolean emulatedEmail;

	private String userid;

	private String userSecret;

	private String sendFrom;

	private String sendTo;

	private boolean emulateEmail;

	public Test test = new Test(); //does'nt works with mail.test.testdata = test

    @Data
	public static class Test {
		private String testdata;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		UserIdStatic = userid;
		this.userid = userid;
	}

	public String getUserSecret() {
		return userSecret;
	}

	public void setUserSecret(String userSecret) {
		userSecretStatic = userSecret;
    	this.userSecret = userSecret;
	}

	public String getSendFrom() {
		return sendFrom;
	}

	public void setSendFrom(String sendFrom) {
		sendFromStatic = sendFrom;
    	this.sendFrom = sendFrom;
	}

	public String getSendTo() {
		return sendTo;
	}

	public void setSendTo(String sendTo) {
		sendToStatic = sendTo;
		this.sendTo = sendTo;
	}

	public boolean isEmulateEmail() {
		return emulateEmail;
	}

	public void setEmulateEmail(boolean emulateEmail) {
    	emulatedEmail = emulateEmail;
		this.emulateEmail = emulateEmail;
	}

	public Test getTest() {
		return test;
	}

	public void setTest(Test test) {
		this.test = test;
	}
}
