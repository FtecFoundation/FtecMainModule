package com.ftec.resources;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "ftec")
public class Resources {
    public static final double startingBalance=0;
    public static final String disabledPassword="NONE";
    public Email email = new Email();
    public Logger logger = new Logger();
    public String appName = "ftec";
    public String base_url="https://ftec.io/";
    public boolean productionFeatures=false;
    public int paginationRescordsPerPage =10;

    public int trialDaysDuration = 7;
    public String defaultLanguage="en";

    public static class Email{
        public String host_name;
        public int smtp_port;
        public boolean tls;
        public boolean ssl;
        public boolean authNeeded;
        public String mailLogin;
        public String mailPassword;
        public String fromName;

        public String getFromName() {
            return fromName;
        }

        public void setFromName(String fromName) {
            this.fromName = fromName;
        }

        public String getMailLogin() {
            return mailLogin;
        }

        public void setMailLogin(String mailLogin) {
            this.mailLogin = mailLogin;
        }

        public String getMailPassword() {
            return mailPassword;
        }

        public void setMailPassword(String mailPassword) {
            this.mailPassword = mailPassword;
        }

        public String getHost_name() {
            return host_name;
        }

        public void setHost_name(String host_name) {
            this.host_name = host_name;
        }

        public int getSmtp_port() {
            return smtp_port;
        }

        public void setSmtp_port(int smtp_port) {
            this.smtp_port = smtp_port;
        }

        public boolean isTls() {
            return tls;
        }

        public void setTls(boolean tls) {
            this.tls = tls;
        }

        public boolean isSsl() {
            return ssl;
        }

        public void setSsl(boolean ssl) {
            this.ssl = ssl;
        }

        public boolean isAuthNeeded() {
            return authNeeded;
        }

        public void setAuthNeeded(boolean authNeeded) {
            this.authNeeded = authNeeded;
        }
    }

    public static class Logger{
        public boolean enabled;
        public String path;
        public int exceptionHeight = 8;

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }
    }

    public boolean isProductionFeatures() {
        return productionFeatures;
    }

    public void setProductionFeatures(boolean productionFeatures) {
        this.productionFeatures = productionFeatures;
    }

    public int getPaginationRescordsPerPage() {
        return paginationRescordsPerPage;
    }

    public void setPaginationRescordsPerPage(int paginationRescordsPerPage) {
        this.paginationRescordsPerPage = paginationRescordsPerPage;
    }

    public int getTrialDaysDuration() {
        return trialDaysDuration;
    }

    public void setTrialDaysDuration(int trialDaysDuration) {
        this.trialDaysDuration = trialDaysDuration;
    }

    public Email getEmail() {
        return email;
    }

    public void setEmail(Email email) {
        this.email = email;
    }

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getBase_url() {
        return base_url;
    }

    public void setBase_url(String base_url) {
        this.base_url = base_url;
    }

    public String getDefaultLanguage() {
        return defaultLanguage;
    }

    public void setDefaultLanguage(String defaultLanguage) {
        this.defaultLanguage = defaultLanguage;
    }
}
