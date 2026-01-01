package com.foodkeeper.foodkeeperserver.food.implement;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.foodkeeper.foodkeeperserver.support.exception.AppException;
import com.foodkeeper.foodkeeperserver.support.exception.ErrorType;
import com.google.common.base.Strings;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3ImageManager implements ImageManager {

    private final AmazonS3 amazonS3;
    private static final String DATE_TIME_FORMATTER = "yyyyMMdd";

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Override
    public Optional<String> fileUpload(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return Optional.empty();
        }
        String url = toUrls(file);

        try {
            InputStream inputStream = file.getInputStream();
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());
            amazonS3.putObject(bucket, url, inputStream, metadata);
            return Optional.of(url);
        } catch (IOException e) {
            throw new AppException(ErrorType.S3_UPLOAD_ERROR);
        }
    }

    @Override
    public String getFullUrl(String fileName) {
        return amazonS3.getUrl(bucket, fileName).toString();
    }

    @Override
    public void deleteFile(String fileName) {
        if (Strings.isNullOrEmpty(fileName)) {
            return;
        }

        try {
            amazonS3.deleteObject(bucket, fileName);
        } catch (Exception e) {
            throw new AppException(ErrorType.S3_DELETE_ERROR);
        }
    }

    private String toUrls(MultipartFile file) {
        LocalDateTime now = LocalDateTime.now();
        String createdDate = now.format(DateTimeFormatter.ofPattern(DATE_TIME_FORMATTER));
        String uuid = UUID.randomUUID().toString();
        String ext = Objects.requireNonNull(file.getOriginalFilename()).substring(file.getOriginalFilename().lastIndexOf("."));
        return createdDate + "/" + uuid + ext;
    }
}
