package com.foodkeeper.foodkeeperserver.notification.implement;

import com.google.firebase.messaging.BatchResponse;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class FcmRetryExecutor {

    @Retryable(
            retryFor = FirebaseMessagingException.class,
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000, multiplier = 2.0)
    )
    public BatchResponse sendEach(List<Message> messages) throws FirebaseMessagingException {
        log.info("[FCM 전송 시도] 대상: {}건", messages.size());
        return FirebaseMessaging.getInstance().sendEach(messages, true);
    }
}
