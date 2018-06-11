package com.ftec.controllers;

import com.ftec.entities.User;
import com.ftec.repositories.UserDAO;
import com.ftec.resources.Resources;
import com.ftec.resources.models.MvcResponse;
import com.ftec.services.interfaces.TokenService;
import com.google.common.io.Files;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@RestController
public class AvatarController {

    private final UserDAO userDAO;
    private static String UPLOADED_FOLDER;

    @GetMapping(value = "/image/getImage", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getImage(HttpServletRequest request) throws IOException {
        String token = request.getHeader(TokenService.TOKEN_NAME);
        Optional<User> userByToken = userDAO.findById(TokenService.getUserIdFromToken(token));

        if (userByToken.isPresent()) {
            long userFromDBId = userByToken.get().getId();
            String fileName = userFromDBId + ".jpg";
            UPLOADED_FOLDER = Resources.uploadPathStatic;
            File file = new File(UPLOADED_FOLDER + fileName);

            if (file.exists()) {
                return com.google.common.io.Files.toByteArray(file);
            } else {
                File DEFAULT_IMAGE = new ClassPathResource("/images/0.jpg").getFile();
                return com.google.common.io.Files.toByteArray(DEFAULT_IMAGE);
            }
        } else {
            return null;
        }
    }

    @GetMapping(value = "image/{imgName}", produces = MediaType.IMAGE_JPEG_VALUE)
    public byte[] getImageByURL(@PathVariable("imgName") String imageName) throws IOException {
        UPLOADED_FOLDER = Resources.uploadPathStatic;
        File file = new File(UPLOADED_FOLDER + imageName);

        if (file.exists()) {
            return com.google.common.io.Files.toByteArray(file);
        }
        return null;
    }

    @PostMapping(value = "/image/deleteImage", consumes = "application/json", produces = "application/json")
    public MvcResponse deleteImage(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String token = request.getHeader(TokenService.TOKEN_NAME);
        Optional<User> userByToken = userDAO.findById(TokenService.getUserIdFromToken(token));

        if (userByToken.isPresent()) {
            if (userByToken.get().getImageName() != null) {
                long userFromDBId = userByToken.get().getId();
                String fileName = userFromDBId + ".jpg";
                UPLOADED_FOLDER = Resources.uploadPathStatic;
                File file = new File(UPLOADED_FOLDER + fileName);

                if (file.delete()) {
                    userByToken.get().setImageName(null);
                    userDAO.save(userByToken.get());
                    return new MvcResponse(200);
                } else {
                    response.setStatus(400);
                    return MvcResponse.getMvcErrorResponse(400, "Something went wrong while deleting file");
                }
            } else {
                response.setStatus(400);
                return MvcResponse.getMvcErrorResponse(400, "This user doesn't have image");
            }
        } else {
            response.setStatus(400);
            return MvcResponse.getMvcErrorResponse(400, "User not found");
        }
    }

    @PostMapping(value = "/image/uploadImage", produces = "application/json")
    public MvcResponse uploadFile(@RequestParam("file") MultipartFile uploadFile, HttpServletRequest request, HttpServletResponse response) {

        if (uploadFile.isEmpty()) {
            return MvcResponse.getMvcErrorResponse(404, "Please select a file");
        }

        try {
            String token = request.getHeader(TokenService.TOKEN_NAME);
            Optional<User> user = userDAO.findById(TokenService.getUserIdFromToken(token));

            if (user.isPresent()) {
                long userFromDBId = user.get().getId();
                String uploadFileNameForUser = userFromDBId + "." + FilenameUtils.getExtension(String.valueOf(uploadFile.getOriginalFilename()));
                user.get().setImageName(uploadFileNameForUser);
                userDAO.save(user.get());

                saveUploadedFiles(uploadFile, uploadFileNameForUser);
            }
        } catch (IOException e) {
            response.setStatus(400);
            return MvcResponse.getMvcErrorResponse(400, "Something wrong with file");
        }
        return new MvcResponse(200, "Successfully uploaded - " + uploadFile.getOriginalFilename());
    }

    private void saveUploadedFiles(MultipartFile file, String fileName) throws IOException {
        byte[] bytes = file.getBytes();
        UPLOADED_FOLDER = Resources.uploadPathStatic;
        Path path = Paths.get(UPLOADED_FOLDER + fileName);
        Files.write(bytes, path.toFile());
    }

    @Autowired
    public AvatarController(UserDAO userDao) {
        this.userDAO = userDao;
    }
}