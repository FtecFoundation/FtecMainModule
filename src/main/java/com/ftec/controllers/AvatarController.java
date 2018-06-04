package com.ftec.controllers;

import com.ftec.resources.models.MvcResponse;
import com.google.common.io.Files;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;

@RestController
public class AvatarController {

    @GetMapping(value = "/image")
    public MvcResponse<byte[]> getFile() throws IOException {
        File file = new File("C:/image.jpg");
        byte[] byteObject = Files.toByteArray(file);
        MvcResponse<byte[]> mvcResponse = new MvcResponse<>();
        mvcResponse.setStatus(HttpStatus.OK);
        mvcResponse.getResponse().put("image", byteObject);

        return mvcResponse;
    }
}