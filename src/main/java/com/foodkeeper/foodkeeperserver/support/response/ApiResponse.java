package com.foodkeeper.foodkeeperserver.support.response;

import com.foodkeeper.foodkeeperserver.support.exception.ErrorMessage;
import com.foodkeeper.foodkeeperserver.support.exception.ErrorType;

public record ApiResponse<T>(
        ResultType result,
        T data,
        ErrorMessage error
) {

    public static ApiResponse<Void> success() {
        return new ApiResponse<>(ResultType.SUCCESS, null, null);
    }

    public static <S> ApiResponse<S> success(S data) {
        return new ApiResponse<>(ResultType.SUCCESS, data, null);
    }

    public static <S> ApiResponse<S> error(ErrorType error, Object errorData) {
        return new ApiResponse<>(ResultType.ERROR, null, ErrorMessage.of(error, errorData));
    }
}
