package com.ftec.services.Implementations;

import com.ftec.services.interfaces.ImageService;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

public class ImageServiceImpl implements ImageService {
    @Override
    public String saveUserImage(MultipartFile file, long userId) {
        return null;
    }

    @Override
    public File getUserImage(long userId) {
        return null;
    }

    @Override
    public String getUserImageUrl(long userId) {
        return null;
    }

    @Override
    public String deleteUserImage(long userId) {
        return null;
    }
}
