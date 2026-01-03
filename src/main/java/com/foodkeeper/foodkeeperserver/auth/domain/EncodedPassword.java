package com.foodkeeper.foodkeeperserver.auth.domain;

import com.foodkeeper.foodkeeperserver.support.exception.AppException;
import com.foodkeeper.foodkeeperserver.support.exception.ErrorType;
import org.apache.logging.log4j.util.Strings;

public record EncodedPassword(String password) {

    public EncodedPassword {
        validatePassword(password);
    }

    private static void validatePassword(String password) {
        if (Strings.isBlank(password)) {
            throw new AppException(ErrorType.PASSWORD_IS_NULL);
        }
    }
}
