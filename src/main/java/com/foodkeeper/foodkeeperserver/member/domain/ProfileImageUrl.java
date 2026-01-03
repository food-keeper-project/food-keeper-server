package com.foodkeeper.foodkeeperserver.member.domain;

import com.foodkeeper.foodkeeperserver.support.exception.AppException;
import com.foodkeeper.foodkeeperserver.support.exception.ErrorType;
import com.google.common.base.Strings;

import java.net.URI;

public record ProfileImageUrl(String imageUrl) {

    public ProfileImageUrl {
        imageUrl = Strings.nullToEmpty(imageUrl);
        validateImageUrl(imageUrl);
    }

    public static ProfileImageUrl empty() {
        return new ProfileImageUrl("");
    }

    private static void validateImageUrl(String imageUrl) {
        if (imageUrl.isEmpty()) {
            return;
        }

        if (isInValidImageUrl(imageUrl)) {
            throw new AppException(ErrorType.INVALID_IMAGE_URL);
        }
    }

    private static boolean isInValidImageUrl(String imageUrl) {
        try {
            URI uri = URI.create(imageUrl);
            return uri.getScheme() == null | uri.getHost() == null;
        } catch (IllegalArgumentException e) {
            return true;
        }
    }
}
