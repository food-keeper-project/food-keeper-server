package com.foodkeeper.foodkeeperserver.member.domain;

import com.foodkeeper.foodkeeperserver.support.exception.AppException;
import com.foodkeeper.foodkeeperserver.support.exception.ErrorType;
import org.apache.logging.log4j.util.Strings;

import java.util.regex.Pattern;

public record Email(String email) {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    public Email {
        validateEmail(email);
    }

    private static void validateEmail(String email) {
        if (Strings.isBlank(email) || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new AppException(ErrorType.INVALID_EMAIL);
        }
    }
}
