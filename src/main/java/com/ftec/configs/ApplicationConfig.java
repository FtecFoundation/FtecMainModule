package com.ftec.configs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@SpringBootApplication(scanBasePackages = {"com.ftec.*"})
@EnableElasticsearchRepositories("com.ftec.repositories")
public class ApplicationConfig extends SpringBootServletInitializer {
    //Also added configuration for tomcat starting
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ApplicationConfig.class);
    }

    public static void main(String[] args) throws Exception {
        SpringApplication.run(ApplicationConfig.class, args);
    }    
    @Bean("messageSource")
    @Profile({"development","test"})
    public MessageSource messageSourceDev(){
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.addBasenames("classpath:locales/Messages");
        messageSource.addBasenames("classpath:locales/ValidationMessages");
        messageSource.setCacheSeconds(10);
        return messageSource;
    }
    @Bean("messageSource")
    @Profile("production")
    public MessageSource messageSourceProd(){
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.addBasenames("classpath:locales/Messages");
        messageSource.addBasenames("classpath:locales/ValidationMessages");
        messageSource.setCacheSeconds(0);
        return messageSource;
    }
}
