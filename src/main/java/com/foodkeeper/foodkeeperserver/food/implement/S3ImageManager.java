package com.foodkeeper.foodkeeperserver.food.implement;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.foodkeeper.foodkeeperserver.support.exception.AppException;
import com.foodkeeper.foodkeeperserver.support.exception.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3ImageManager implements ImageManager {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Async
    @Override
    public String fileUpload(MultipartFile file) {
        if(file == null || file.isEmpty()){
            return "";
        }
        String url = toUrls(file);

        try {
            InputStream inputStream = file.getInputStream();
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());
            amazonS3.putObject(bucket, url, inputStream, metadata);
            return url;
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
        try {
            amazonS3.deleteObject(bucket, fileName);
        } catch (Exception e) {
            throw new AppException(ErrorType.S3_UPLOAD_ERROR);
        }
    }

    private String toUrls(MultipartFile file) {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter dateTimeFormatter =
                DateTimeFormatter.ofPattern("yyyyMMdd");
        String createdDate = now.format(dateTimeFormatter);
        String uuid = UUID.randomUUID().toString();
        String ext = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        return createdDate + "/" + uuid + ext;
    }
}
