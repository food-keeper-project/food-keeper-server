package com.foodkeeper.foodkeeperserver.common.controller;

import com.foodkeeper.foodkeeperserver.support.exception.AppException;
import com.foodkeeper.foodkeeperserver.support.exception.ErrorType;
import com.foodkeeper.foodkeeperserver.support.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ApiControllerAdvice {

    @NullMarked
    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse<@Nullable Object>> handleAppException(AppException e) {
        switch (e.getErrorType().getLogLevel()) {
            case ERROR -> log.error("[AppException]: {}", e.getMessage());
            case WARN -> log.warn("[AppException]: {}", e.getMessage());
            default -> log.info("[AppException]: {}", e.getMessage());
        }

        return new ResponseEntity<>(ApiResponse.error(e.getErrorType(), e.getData()), e.getErrorType().getStatus());
    }

    @NullMarked
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<@Nullable Object>> handleException(Exception e) {
        log.error("[Exception]: {}", e.getMessage());
        return new ResponseEntity<>(ApiResponse.error(ErrorType.DEFAULT_ERROR, e), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
