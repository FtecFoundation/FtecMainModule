package com.ftec.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ftec.configs.ApplicationConfig;
import com.ftec.resources.models.MvcResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.Assert.assertEquals;


@ActiveProfiles(value = "jenkins-tests,test", inheritProfiles = false)
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT,classes = ApplicationConfig.class)
@AutoConfigureMockMvc
public class MvcResponseTest {

	@Autowired
	MockMvc mvc;

	@Test
	public void POJOTest() throws Exception {
		MvcResponse mvcResponse = new MvcResponse(400, "pojo", new Pojo("test"));
		ObjectMapper objectMapper = new ObjectMapper();
		JSONObject object = new JSONObject();
		JSONObject response = new JSONObject();
		JSONObject pojo = new JSONObject();
		pojo.put("field", "test");
		response.put("pojo", pojo);
		object.put("response", response);
		object.put("status", 400);
		//Wrap jackson string by JSONObject to prevent ordering issues
		assertEquals(object.toString(), new JSONObject(objectMapper.writeValueAsString(mvcResponse)).toString());
	}

	@Data
	@AllArgsConstructor
	private class Pojo{
		String field;
	}
}