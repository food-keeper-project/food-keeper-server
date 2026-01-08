package com.foodkeeper.foodkeeperserver.auth.domain;

import com.foodkeeper.foodkeeperserver.support.exception.AppException;
import com.foodkeeper.foodkeeperserver.support.exception.ErrorType;
import org.apache.logging.log4j.util.Strings;

import java.util.regex.Pattern;

public record LocalAccount(String account) {
    private static final int ACCOUNT_MIN_LENGTH = 6;
    private static final int ACCOUNT_MAX_LENGTH = 12;
    private static final Pattern ACCOUNT_PATTERN = Pattern.compile("^[a-zA-Z0-9]+$");

    public LocalAccount {
        validateAccount(account);
    }

    private static void validateAccount(String account) {
        if (Strings.isBlank(account)) {
            throw new AppException(ErrorType.ACCOUNT_IS_NULL);
        }

        if (account.length() > ACCOUNT_MAX_LENGTH || account.length() < ACCOUNT_MIN_LENGTH) {
            throw new AppException(ErrorType.INVALID_ACCOUNT_LENGTH);
        }

        if (!ACCOUNT_PATTERN.matcher(account).matches()) {
            throw new AppException(ErrorType.INVALID_ACCOUNT_FORMAT); // 특수문자, 한글 등 포함 시 발생
        }
    }
}
