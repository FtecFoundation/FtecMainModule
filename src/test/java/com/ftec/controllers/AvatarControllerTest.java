package com.ftec.controllers;

import com.ftec.configs.ApplicationConfig;
import com.ftec.entities.User;
import com.ftec.repositories.UserDAO;
import com.ftec.repositories.TokenDAO;
import com.ftec.services.TokenService;
import com.ftec.utils.EntityGenerator;
import com.google.common.io.Files;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.File;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles(value = "jenkins-tests,test", inheritProfiles = false)
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = ApplicationConfig.class)
@AutoConfigureMockMvc
public class AvatarControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    TokenDAO tokenDAO;

    @Autowired
    TokenService tokenService;

    @Autowired
    UserDAO userDAO;

    @Before
    public void setUp() {
        userDAO.deleteAll();
        tokenDAO.deleteAll();
    }

    @Test
    public void getImageTest() throws Exception {

        // try to make defaultImage in MediaType.IMAGE_JPEG_VALUE
        File defaultImage = new ClassPathResource("/images/0.jpg").getFile();
        byte[] encoded = Base64.encodeBase64(FileUtils.readFileToByteArray(defaultImage));

        User user = EntityGenerator.getNewUser();
        userDAO.save(user);

        String token = tokenService.createSaveAndGetNewToken(user.getId());

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get("http://localhost:8080/getImage")
                .header(TokenService.TOKEN_NAME, token)
                .contentType(MediaType.IMAGE_JPEG_VALUE)
                .accept(MediaType.ALL_VALUE))
                .andDo(print()).andExpect(status().isOk()).andReturn();

//        String content = mvcResult.getResponse().getContentAsString();
//        byte[] contentInBytesEncoded = Base64.encodeBase64(content.getBytes());
//        assertEquals(contentInBytesEncoded, encoded);
    }

    /*@Test
    public void uploadFileTest() throws Exception {

        User user = EntityGenerator.getNewUser();
        userDAO.save(user);
        String token = tokenService.createSaveAndGetNewToken(user.getId());

        File defaultImage = new ClassPathResource("/images/0.jpg").getFile();
        byte[] bytesFromFile = Files.toByteArray(defaultImage);
        String str = new String(bytesFromFile);
        JSONObject object = new JSONObject();
        object.put("file", str);

        MockMultipartFile mockedFile = new MockMultipartFile("file", bytesFromFile);

        //TODO find not deprecated alternative
        mvc.perform(MockMvcRequestBuilders.fileUpload("http://localhost:8080/upload")
                .file(mockedFile)
                .header(TokenService.TOKEN_NAME, token)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }*/

}
