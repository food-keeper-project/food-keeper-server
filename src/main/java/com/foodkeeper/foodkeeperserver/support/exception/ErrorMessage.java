package com.foodkeeper.foodkeeperserver.support.exception;

public record ErrorMessage(
        String errorCode,
        String message,
        Object data
) {
    public static ErrorMessage of(ErrorType errorType, Object data) {
        return new ErrorMessage(errorType.getErrorCode().name(), errorType.getMessage(), data);
    }
}
