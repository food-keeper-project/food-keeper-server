package com.foodkeeper.foodkeeperserver.notification.implement;


import com.foodkeeper.foodkeeperserver.notification.domain.AlarmMessages;
import com.google.firebase.messaging.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class FcmSender {

    private final FcmManager fcmManager;
    private final FcmRetryExecutor fcmRetryExecutor;

    @Async("fcmExecutor")
    public void sendNotification(AlarmMessages alarmMessages) {
        List<Message> messages = alarmMessages.messages();
        List<String> tokens = alarmMessages.tokens();

        try {
            BatchResponse batchResponse = fcmRetryExecutor.sendEach(messages);

            List<String> invalidTokens = extractInvalidTokens(tokens, batchResponse);
            fcmManager.removeAll(invalidTokens);

            List<Message> retryableMessages = extractRetryableMessages(messages, tokens, batchResponse);
            if (!retryableMessages.isEmpty()) {
                log.info("[FCM 재시도] 네트워크 오류로 실패한 {}건 재시도", retryableMessages.size());
                fcmRetryExecutor.sendEach(retryableMessages);
            }

        } catch (FirebaseMessagingException e) {
            log.warn("[FCM 전송 최종 실패] error: {}", e.getMessage());
        }
    }

    private List<String> extractInvalidTokens(List<String> tokens, BatchResponse batchResponse) {
        List<SendResponse> responses = batchResponse.getResponses();
        List<String> invalidTokens = new ArrayList<>();

        for (int i = 0; i < responses.size(); i++) {
            SendResponse response = responses.get(i);

            if (!response.isSuccessful()) {
                MessagingErrorCode errorCode = response.getException().getMessagingErrorCode();

                if (errorCode == MessagingErrorCode.UNREGISTERED ||
                        errorCode == MessagingErrorCode.INVALID_ARGUMENT) {
                    invalidTokens.add(tokens.get(i));
                }
            }
        }
        return invalidTokens;
    }

    private List<Message> extractRetryableMessages(List<Message> messages, List<String> tokens, BatchResponse batchResponse) {
        List<SendResponse> responses = batchResponse.getResponses();
        List<Message> retryableMessages = new ArrayList<>();

        for (int i = 0; i < responses.size(); i++) {
            SendResponse response = responses.get(i);

            if (!response.isSuccessful()) {
                MessagingErrorCode errorCode = response.getException().getMessagingErrorCode();

                if (errorCode == MessagingErrorCode.UNAVAILABLE ||
                        errorCode == MessagingErrorCode.INTERNAL ||
                        errorCode == MessagingErrorCode.QUOTA_EXCEEDED) {
                    retryableMessages.add(messages.get(i));
                } else if (errorCode != MessagingErrorCode.UNREGISTERED &&
                        errorCode != MessagingErrorCode.INVALID_ARGUMENT) {
                    log.error("[FCM 개별 전송 실패] token: {}, error: {}", tokens.get(i), response.getException().getMessage());
                }
            }
        }
        return retryableMessages;
    }
}
