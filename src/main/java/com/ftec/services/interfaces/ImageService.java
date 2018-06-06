package com.ftec.services.interfaces;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public interface ImageService {
    String saveUserImage(MultipartFile file, long userId);
    File getUserImage(long userId);
    String getUserImageUrl(long userId);
    String deleteUserImage(long userId);
}
