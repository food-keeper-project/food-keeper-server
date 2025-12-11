package com.foodkeeper.foodkeeperserver.support.exception;

import lombok.Getter;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorType {
    NOT_FOUND_DATA(HttpStatus.BAD_REQUEST, ErrorCode.E401, "해당 데이터를 찾을 수 없습니다.", LogLevel.ERROR);

    private final HttpStatus status;
    private final ErrorCode errorCode;
    private final String message;
    private final LogLevel logLevel;

    ErrorType(HttpStatus status, ErrorCode errorCode, String message, LogLevel logLevel) {
        this.status = status;
        this.errorCode = errorCode;
        this.message = message;
        this.logLevel = logLevel;
    }
}
