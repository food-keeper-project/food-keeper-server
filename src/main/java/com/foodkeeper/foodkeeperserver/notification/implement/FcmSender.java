package com.foodkeeper.foodkeeperserver.notification.implement;


import com.foodkeeper.foodkeeperserver.notification.domain.FcmMessage;
import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class FcmSender {

    private final FcmManager fcmManager;

    @Async("fcmExecutor")
    public void sendNotification(FcmMessage sender) {

        Map<String, String> data = new HashMap<>();
        data.put("foodName", sender.foodName());
        data.put("remainingDays", String.valueOf(sender.remainingDays()));
        data.put("title", sender.title());
        data.put("type", sender.type());

        AndroidConfig androidConfig = AndroidConfig.builder()
                .setPriority(AndroidConfig.Priority.HIGH)
                .build();

        Message message = Message.builder()
                .setToken(sender.fcmToken())
                .putAllData(data)
                .setAndroidConfig(androidConfig)
                .build();
        try {
            FirebaseMessaging.getInstance().send(message);

        } catch (FirebaseMessagingException e) {
            MessagingErrorCode errorCode = e.getMessagingErrorCode();

            if (errorCode == MessagingErrorCode.UNREGISTERED ||
                    errorCode == MessagingErrorCode.INVALID_ARGUMENT) {

                log.warn("[FCM 만료 토큰 삭제] fcmToken: {}", sender.fcmToken());
                fcmManager.remove(sender.fcmToken());
            }
            log.error("[FCM 전송 실패] fcmToken: {}, error: {}", sender.fcmToken(), e.getMessage());
        }
    }


}
