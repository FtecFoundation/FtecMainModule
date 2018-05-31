package com.ftec.resources;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
public class EntitiesNames {
    @Profile("development")
    @Bean(name = "idsIndex")
    public String getIndexNameDevelop() {
        return "ids";
    }
    @Profile("test")
    @Bean(name = "idsIndex")
    public String getIndexNameTest() {
        return "ids_test";
    }
}
