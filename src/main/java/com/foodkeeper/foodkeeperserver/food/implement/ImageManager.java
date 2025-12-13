package com.foodkeeper.foodkeeperserver.food.implement;

import org.springframework.web.multipart.MultipartFile;

public interface ImageManager {

    String fileUpload(MultipartFile file);

    String getFullUrl(String fileName);

    void deleteFile(String fileName);
}
