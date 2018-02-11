package com.ftec.resources;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "ftec")
public class Resources {
    public Email email = new Email();
    public Logger logger = new Logger();
    public String appName = "ftec";
    public String base_url="https://ftec.io/";
    public boolean productionFeatures=false;
    public int paginationDefault =10;

    public int trialDaysDuration = 7;
    public String defaultLanguage="en";

    private static class Email{
        public String host_name;
        public int smtp_port;
        public boolean tls;
        public boolean ssl;
        public boolean authNeeded;

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

    private static class Logger{
        public boolean enabled;
        public String path;

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