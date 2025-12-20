package com.foodkeeper.foodkeeperserver.member.domain;

import ch.qos.logback.core.util.StringUtil;
import com.foodkeeper.foodkeeperserver.auth.domain.MemberRoles;
import com.foodkeeper.foodkeeperserver.member.domain.enums.SignUpType;
import com.foodkeeper.foodkeeperserver.support.exception.AppException;
import com.foodkeeper.foodkeeperserver.support.exception.ErrorType;
import lombok.Builder;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

import java.net.URI;
import java.util.regex.Pattern;

@NullMarked
@Builder
public record NewMember(String email,
                        @Nullable String nickname,
                        @Nullable String imageUrl,
                        SignUpType signUpType,
                        String signUpIpAddress,
                        MemberRoles memberRoles) {

    private static final int NICKNAME_MAX_LENGTH = 20;
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");
    private static final Pattern IPV4_PATTERN = Pattern.compile("^(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)" +
                            "(\\.(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)){3}$");

    public NewMember {
        nickname = StringUtil.nullStringToEmpty(nickname);
        imageUrl = StringUtil.nullStringToEmpty(imageUrl);

        if (nickname.length() > NICKNAME_MAX_LENGTH) {
            throw new AppException(ErrorType.INVALID_NICKNAME_LENGTH);
        }

        if (isInvalidEmail(email)) {
            throw new AppException(ErrorType.INVALID_EMAIL);
        }

        if (!StringUtil.isNullOrEmpty(imageUrl) && isInValidImageUrl(imageUrl)) {
            throw new AppException(ErrorType.INVALID_IMAGE_URL);
        }

        if (isInvalidIpAddress(signUpIpAddress)) {
            throw new AppException(ErrorType.INVALID_ACCESS_PATH);
        }
    }

    private static boolean isInvalidEmail(String email) {
        return email == null || !EMAIL_PATTERN.matcher(email).matches();
    }

    private static boolean isInValidImageUrl(String imageUrl) {
        try {
            URI uri = URI.create(imageUrl);
            return uri.getScheme() == null | uri.getHost() == null;
        } catch (IllegalArgumentException e) {
            return true;
        }
    }

    private static boolean isInvalidIpAddress(String ipAddress) {
        return ipAddress == null || !IPV4_PATTERN.matcher(ipAddress).matches();
    }
}
