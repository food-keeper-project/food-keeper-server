package com.foodkeeper.foodkeeperserver.notification.business;

import com.foodkeeper.foodkeeperserver.notification.domain.FcmMessage;
import com.foodkeeper.foodkeeperserver.notification.implement.FcmManager;
import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotifyService {

    private final FcmManager fcmManager;

    @Async("fcmExecutor")
    public void sendNotification(FcmMessage sender) {

        Notification notification = Notification.builder()
                .setTitle(sender.title())
                .setBody(sender.body())
                .build();

        Message message = Message.builder()
                .setToken(sender.fcmToken())
                .setNotification(notification)
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
