package com.ftec.resources;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Configuration
@ConfigurationProperties(prefix = "ftec")
public class Resources {

	public static String UserIdStatic;
	public static String userSecretStatic;
	public static String sendFromStatic;
	public static String sendToStatic;
	public static boolean emulatedEmail;
	public static String uploadPathStatic;
	public static boolean loggerEnabledStatic;
	public static String domainUrlStatic;
	public static boolean doPrintStatic;

	private String userId;

	private String userSecret;

	private String sendFrom;

	private String sendTo;

	private boolean emulateEmail;

	private String uploadPath;

	private boolean loggerEnabled;

	private String domainUrl;

	private boolean doPrint;

	public void setDoPrint(boolean doPrint) {
		doPrintStatic = doPrint;
		this.doPrint = doPrint;
	}


	public void setLoggerEnabled(boolean loggerEnabled) {
		loggerEnabledStatic = loggerEnabled;
		this.loggerEnabled = loggerEnabled;
	}

	public void setDomainUrl(String domainUrl) {
		domainUrlStatic = domainUrl;
		this.domainUrl = domainUrl;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		UserIdStatic = userId;
		this.userId = userId;
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

	public String getUploadPath() {
		return uploadPath;
	}

	public void setUploadPath(String uploadPath) {
		uploadPathStatic = uploadPath;
		this.uploadPath = uploadPath;
	}
}
