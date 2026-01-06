package com.foodkeeper.foodkeeperserver.auth.domain;

import com.foodkeeper.foodkeeperserver.member.domain.Email;
import com.foodkeeper.foodkeeperserver.support.exception.AppException;
import com.foodkeeper.foodkeeperserver.support.exception.ErrorType;

import java.util.regex.Pattern;

public record EmailCode(Email email, String code) {
    private static final Pattern CODE_PATTERN = Pattern.compile("^[1-9][0-9]{5}$");

    public EmailCode {
        if (code == null) {
            throw new AppException(ErrorType.INVALID_EMAIL_CODE);
        }

        if (!CODE_PATTERN.matcher(code).matches()) {
            throw new AppException(ErrorType.INVALID_EMAIL_CODE);
        }
    }

    public static EmailCode of(String email, String code) {
        return new EmailCode(new Email(email), code);
    }

    public String getEmail() {
        return email.email();
    }
}
