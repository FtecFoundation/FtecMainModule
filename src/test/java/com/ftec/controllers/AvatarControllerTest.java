package com.ftec.controllers;

import com.ftec.configs.ApplicationConfig;
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

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ApplicationConfig.class)
@AutoConfigureMockMvc
public class AvatarControllerTest {

    @Autowired
    MockMvc mvc;

    @Test
    public void AvatarByteCodeTest() throws Exception {
        MvcResult result = mvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/image")
                .contentType(MediaType.ALL_VALUE)
                .accept(MediaType.ALL_VALUE))
                .andDo(print()).andReturn();

        String jsonResponse = result.getResponse().getContentAsString();
//        System.out.println(jsonResponse);
    }
}
