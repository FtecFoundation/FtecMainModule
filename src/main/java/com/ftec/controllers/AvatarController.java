package com.ftec.controllers;

import com.ftec.entities.User;
import com.ftec.repositories.UserDAO;
import com.ftec.services.TokenService;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
public class AvatarController {
    private final UserDAO userDAO;

    @Autowired
    public AvatarController(UserDAO userDao) {
        this.userDAO = userDao;
    }

    private static String UPLOADED_FOLDER = "C://Images//";
    private static File DEFAULT_IMAGE = new File(UPLOADED_FOLDER + 0 + ".jpg");


    @GetMapping(value = "/getImage", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getImage(HttpServletRequest request) throws IOException {
        long userFromDBId = userDAO.findById(TokenService.getUserIdFromToken(request)).get().getId();

        String fileName = userFromDBId + ".jpg";
        File file = new File(UPLOADED_FOLDER + fileName);
        if (file.exists()) {
            return com.google.common.io.Files.toByteArray(file);
        } else {
            return com.google.common.io.Files.toByteArray(DEFAULT_IMAGE);
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile uploadFile, HttpServletRequest request) {

        if (uploadFile.isEmpty()) {
            return new ResponseEntity<Object>("Please select a file", HttpStatus.BAD_REQUEST);
        }

        try {
            long userFromDBId = userDAO.findById(TokenService.getUserIdFromToken(request)).get().getId();
            saveUploadedFiles(uploadFile, userFromDBId);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<Object>("Successfully uploaded - " + uploadFile.getOriginalFilename(), HttpStatus.OK);
    }

    private void saveUploadedFiles(MultipartFile file, long userId) throws IOException {
        String filename = String.valueOf(userId) + "." + FilenameUtils.getExtension(String.valueOf(file.getOriginalFilename()));
        byte[] bytes = file.getBytes();
        Path path = Paths.get(UPLOADED_FOLDER + filename);
        Files.write(path, bytes);
    }


}