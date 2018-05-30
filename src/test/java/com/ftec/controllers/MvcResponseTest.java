package com.ftec.controllers;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ftec.configs.ApplicationConfig;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT,classes = ApplicationConfig.class)
@AutoConfigureMockMvc
public class MvcResponseTest {
	
	@Autowired
	MockMvc mvc;
	
	@Test
	public void POJOtest() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
	
		MvcResult result = mvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/testMvcResponse")
				.contentType(MediaType.ALL_VALUE)
				.accept(MediaType.ALL_VALUE))
		.andDo(print()).andReturn();

		 String jsonResponse = result.getResponse().getContentAsString();
		 
		 JSONObject o = new JSONObject(jsonResponse);
		 JSONObject jsonObject = o.getJSONObject("response");
		 JSONObject ob = (JSONObject) jsonObject.get("user");

		 String email = (String) ob.get("email");		 

		 assertTrue(email.equals("emailTest"));
	}
}
