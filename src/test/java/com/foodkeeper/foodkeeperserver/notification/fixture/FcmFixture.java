package com.foodkeeper.foodkeeperserver.notification.fixture;

import com.foodkeeper.foodkeeperserver.notification.domain.FcmToken;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FcmFixture {

    public static FcmToken createMock(Long id, String token, String createdAt, String updatedAt) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return new FcmToken(
                id,
                token,
                LocalDateTime.parse(createdAt, formatter),
                LocalDateTime.parse(updatedAt, formatter),
                "default-member-key"
        );
    }


}
