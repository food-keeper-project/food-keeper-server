package com.foodkeeper.foodkeeperserver.notification.fixture;

import com.foodkeeper.foodkeeperserver.notification.dataaccess.entity.FcmTokenEntity;
import com.foodkeeper.foodkeeperserver.notification.domain.FcmToken;
import org.springframework.test.util.ReflectionTestUtils;

public class FcmFixture {

    public static FcmToken createMock(Long id, String token, String memberKey) {
        return new FcmToken(
                id,
                token,
                null,
                null,
                memberKey
        );
    }
    public static FcmTokenEntity createEntity(Long id,String token, String memberKey) {
        FcmTokenEntity fcmToken = FcmTokenEntity.from(createMock(id, token, memberKey));
        ReflectionTestUtils.setField(fcmToken,"id",id);
        return fcmToken;
    }

}
