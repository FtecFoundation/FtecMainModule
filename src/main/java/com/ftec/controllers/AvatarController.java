package com.ftec.controllers;

import org.apache.commons.fileupload.FileItem;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;

@RestController
public class AvatarController {

/*    @PostMapping(value = "/image")
    public byte[] getByteCodeOfImage() throws IOException {
        File fi = new File("C:/image.jpg");
        return Files.readAllBytes(fi.toPath());

    }*/

    @GetMapping(value = "/image")
    public MultipartFile getMultiPartFromImage() throws Exception {
        BufferedImage originalImage = ImageIO.read(new File("C:/image.jpg"));
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(originalImage, "jpg", baos);
        baos.flush();
//        MultipartFile multipartFile = new CommonsMultipartFile();
        return null;
    }
}
