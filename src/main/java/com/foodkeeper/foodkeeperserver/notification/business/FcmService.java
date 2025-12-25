package com.foodkeeper.foodkeeperserver.notification.business;

import com.foodkeeper.foodkeeperserver.notification.implement.FcmManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FcmService {

    private final FcmManager fcmManager;

    public void update(String fcmToken, String memberKey) {
        fcmManager.addTokenOrUpdate(fcmToken, memberKey);
    }

}
