package com.ftec.resources;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class EntitiesNames {
    @Profile("development")
    @Bean(name = "idsIndex")
    public String getIndexNameDevelop() {
        return "ids_dev";
    }
    @Profile("test")
    @Bean(name = "idsIndex")
    public String getIndexNameTest() {
        return "ids_test";
    }
    
    @Profile("development")
    @Bean(name = "users")
    public String getUserNameDeveloper() {
        return "users_dev";
    }
    
    @Profile("test")
    @Bean(name = "users")
    public String getUserNameTest() {
        return "users_test";
    }
}
