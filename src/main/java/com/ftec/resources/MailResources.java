package com.ftec.resources;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "mail")
public class MailResources {

	public String userid;
	
	public String userSecret;
	
	public String sendFrom;
	
	public String sendTo;

	public Test test = new Test(); //does'nt works with mail.test.testdata = test

    @Data
	public static class Test {
		private String testdata;
	}
	
}
