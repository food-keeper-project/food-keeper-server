package com.foodkeeper.foodkeeperserver.notification.implement;


import com.foodkeeper.foodkeeperserver.notification.domain.AlarmMessages;
import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class FcmSender {

    private final FcmManager fcmManager;

    @Async("fcmExecutor")
    public void sendNotification(AlarmMessages alarmMessages) {
        List<Message> messages = alarmMessages.messages();
        List<String> tokens = alarmMessages.tokens();

        try {
            BatchResponse batchResponse = FirebaseMessaging.getInstance().sendEach(messages, true);
            List<String> invalidTokens = extractInvalidTokens(tokens,batchResponse);
            fcmManager.removeAll(invalidTokens);
        } catch (FirebaseMessagingException e) {
            log.error("[FCM 전송 실패] error: {}", e.getMessage());
        }
    }

    private List<String> extractInvalidTokens(List<String> tokens, BatchResponse batchResponse) {
        List<SendResponse> responses = batchResponse.getResponses();
        List<String> invalidTokens = new ArrayList<>();

        for (int i = 0; i < responses.size(); i++) {
            SendResponse response = responses.get(i);

            if (!response.isSuccessful()) {
                FirebaseMessagingException exception = response.getException();
                MessagingErrorCode errorCode = exception.getMessagingErrorCode();
                String failedToken = tokens.get(i);

                if (errorCode == MessagingErrorCode.UNREGISTERED ||
                        errorCode == MessagingErrorCode.INVALID_ARGUMENT) {
                    invalidTokens.add(failedToken);
                } else {
                    log.error("[FCM 개별 전송 실패] token: {}, error: {}", failedToken, exception.getMessage());
                }
            }
        }
        return invalidTokens;
    }
}
