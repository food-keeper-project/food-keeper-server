package com.foodkeeper.foodkeeperserver.common.controller;

import com.foodkeeper.foodkeeperserver.support.exception.AppException;
import com.foodkeeper.foodkeeperserver.support.exception.ErrorCode;
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
        printAppExceptionLog(e);

        return new ResponseEntity<>(ApiResponse.error(e.getErrorType(), e.getData()), e.getErrorType().getStatus());
    }

    @NullMarked
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<@Nullable Object>> handleException(Exception e) {
        printExceptionLog(e);
        return new ResponseEntity<>(ApiResponse.error(ErrorType.DEFAULT_ERROR, e), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private void printAppExceptionLog(AppException e) {
        String className = e.getStackTrace()[0].getClassName();
        String methodName = e.getStackTrace()[0].getMethodName();
        int lineNumber = e.getStackTrace()[0].getLineNumber();
        int status = e.getErrorType().getStatus().value();
        ErrorCode errorCode = e.getErrorType().getErrorCode();
        String message = e.getMessage();
        Object data = e.getData();

        switch (e.getErrorType().getLogLevel()) {
            case ERROR ->
                    log.error("[AppException]: class={} | method={} | line={} | status={} | errorCode={} | message={} | data={}",
                            className, methodName, lineNumber, status, errorCode, message, data);
            case WARN ->
                    log.warn("[AppException]: class={} | method={} | line={} | status={} | errorCode={} | message={} | data={}",
                            className, methodName, lineNumber, status, errorCode, message, data);
            default ->
                    log.info("[AppException]: class={} | method={} | line={} | status={} | errorCode={} | message={} | data={}",
                            className, methodName, lineNumber, status, errorCode, message, data);
        }
    }

    private void printExceptionLog(Exception e) {
        String className = e.getStackTrace()[0].getClassName();
        String methodName = e.getStackTrace()[0].getMethodName();
        int lineNumber = e.getStackTrace()[0].getLineNumber();
        String message = e.getMessage();

        log.error("[AppException]: class={} | method={} | line={} | message={}",
                className, methodName, lineNumber, message);
    }
}
