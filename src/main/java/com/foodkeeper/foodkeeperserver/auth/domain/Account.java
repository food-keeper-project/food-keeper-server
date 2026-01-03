package com.foodkeeper.foodkeeperserver.auth.domain;

import com.foodkeeper.foodkeeperserver.support.exception.AppException;
import com.foodkeeper.foodkeeperserver.support.exception.ErrorType;
import org.apache.logging.log4j.util.Strings;

public record Account(String account) {
    private static final int ACCOUNT_MAX_LENGTH = 20;

    public Account {
        validateAccount(account);
    }

    private static void validateAccount(String account) {
        if (Strings.isBlank(account)) {
            throw new AppException(ErrorType.ACCOUNT_IS_NULL);
        }

        if (account.length() > ACCOUNT_MAX_LENGTH) {
            throw new AppException(ErrorType.INVALID_ACCOUNT_LENGTH);
        }
    }
}
