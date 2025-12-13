package com.foodkeeper.foodkeeperserver.support.exception;

import lombok.Getter;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorType {
    DEFAULT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.E500, "알 수 없는 오류가 발생했습니다. 잠시 후 다시 시도해주세요.", LogLevel.ERROR),
    NOT_FOUND_DATA(HttpStatus.BAD_REQUEST, ErrorCode.E401, "해당 데이터를 찾을 수 없습니다.", LogLevel.ERROR),
    S3_UPLOAD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,ErrorCode.E5000,"이미지 업로드에 실패했습니다.", LogLevel.ERROR),
    CATEGORY_SELECT_ERROR(HttpStatus.BAD_REQUEST,ErrorCode.E1000,"카테고리 선택 개수가 옳바르지 않습니다.",LogLevel.ERROR);

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
