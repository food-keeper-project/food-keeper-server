package com.foodkeeper.foodkeeperserver.auth.domain;

import com.foodkeeper.foodkeeperserver.support.exception.AppException;
import com.foodkeeper.foodkeeperserver.support.exception.ErrorType;
import org.apache.logging.log4j.util.Strings;

import java.util.regex.Pattern;

public record Password(String password) {
    private static final int PASSWORD_MIN_LENGTH = 8;
    private static final int PASSWORD_MAX_LENGTH = 20;
    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z0-9]+$");

    public Password {
        validatePassword(password);
    }

    private static void validatePassword(String password) {
        if (Strings.isBlank(password)) {
            throw new AppException(ErrorType.PASSWORD_IS_NULL);
        }

        if (password.length() > PASSWORD_MAX_LENGTH || password.length() < PASSWORD_MIN_LENGTH) {
            throw new AppException(ErrorType.INVALID_PASSWORD_LENGTH);
        }

        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            throw new AppException(ErrorType.INVALID_PASSWORD_FORMAT);
        }
    }
}
