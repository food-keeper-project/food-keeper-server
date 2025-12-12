package com.foodkeeper.foodkeeperserver.common.utils;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    String toUrls(final MultipartFile file);

    void fileUpload(final MultipartFile file, final String url);

    void deleteFile(final String fileName);
}
