package com.ftec.configs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
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
}
