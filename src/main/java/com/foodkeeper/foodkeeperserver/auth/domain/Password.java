package com.foodkeeper.foodkeeperserver.auth.domain;

import com.foodkeeper.foodkeeperserver.support.exception.AppException;
import com.foodkeeper.foodkeeperserver.support.exception.ErrorType;
import org.apache.logging.log4j.util.Strings;

public record Password(String password) {
    private static final int PASSWORD_MAX_LENGTH = 20;

    public Password {
        validatePassword(password);
    }

    private static void validatePassword(String password) {
        if (Strings.isBlank(password)) {
            throw new AppException(ErrorType.PASSWORD_IS_NULL);
        }

        if (password.length() > PASSWORD_MAX_LENGTH) {
            throw new AppException(ErrorType.INVALID_PASSWORD_LENGTH);
        }
    }
}
