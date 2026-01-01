package com.foodkeeper.foodkeeperserver.food.implement;

import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public interface ImageManager {

    Optional<String> fileUpload(MultipartFile file);

    String getFullUrl(String fileName);

    void deleteFile(String fileName);
}
