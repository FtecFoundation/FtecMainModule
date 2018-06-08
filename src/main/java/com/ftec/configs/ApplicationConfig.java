package com.ftec.configs;

import com.ftec.entities.Ticket;
import com.ftec.repositories.TicketDAO;
import com.ftec.resources.enums.TicketStatus;
import com.ftec.services.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {"com.ftec.*"})
@EnableJpaRepositories(value = {"com.ftec.repositories"})
@EntityScan("com.ftec.entities")
@EnableScheduling
public class ApplicationConfig extends SpringBootServletInitializer implements  CommandLineRunner{
    @Autowired
    TicketDAO ticketDAO;//TODO delete


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

    @Override
    public void run(String... args) throws Exception {

    }
}
