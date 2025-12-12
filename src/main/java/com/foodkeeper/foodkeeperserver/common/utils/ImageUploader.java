package com.foodkeeper.foodkeeperserver.common.utils;

import org.springframework.web.multipart.MultipartFile;

public interface ImageUploader {
    String toUrls(MultipartFile file);

    void fileUpload(MultipartFile file,String url);

    String getFullUrl(String fileName);

    void deleteFile(String fileName);
}
