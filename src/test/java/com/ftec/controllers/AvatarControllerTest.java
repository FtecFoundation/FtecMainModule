package com.ftec.controllers;

import com.ftec.configs.ApplicationConfig;
import com.google.common.io.Files;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ApplicationConfig.class)
@AutoConfigureMockMvc
public class AvatarControllerTest {

    @Autowired
    MockMvc mvc;

    @Test
    public void getFileTest() throws Exception {
        File file = new File("C:/image.jpg");
        byte[] byteObject = Files.toByteArray(file);
//        String mockedFile = new BASE64Encoder().encode(byteObject);

        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/image")
                .contentType(MediaType.ALL_VALUE)
                .accept(MediaType.ALL_VALUE))
                .andDo(print()).andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
        JSONObject o = new JSONObject(jsonResponse);
        JSONObject jsonResp = o.getJSONObject("response");
        byte[] jsonImageString = jsonResp.getString("image").getBytes();


        assertEquals(jsonImageString, byteObject);

    }
}
