package com.foodkeeper.foodkeeperserver.notification.implement;

import com.foodkeeper.foodkeeperserver.notification.domain.AlarmMessages;
import com.google.firebase.messaging.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FcmSenderTest {

    @InjectMocks
    private FcmSender fcmSender;

    @Mock
    private FcmManager fcmManager;

    @Mock
    private FcmRetryExecutor fcmRetryExecutor;

    @Test
    @DisplayName("알림 전송 요청 시 fcmRetryExecutor.sendEach 호출 성공")
    void sendNotification_SUCCESS() throws Exception {
        // given
        Message message = Message.builder().setToken("validToken").build();
        AlarmMessages alarmMessages = new AlarmMessages(List.of(message), List.of("validToken"));

        BatchResponse batchResponse = mock(BatchResponse.class);
        SendResponse sendResponse = mock(SendResponse.class);

        given(fcmRetryExecutor.sendEach(anyList())).willReturn(batchResponse);
        given(batchResponse.getResponses()).willReturn(List.of(sendResponse));
        given(sendResponse.isSuccessful()).willReturn(true);

        // when
        fcmSender.sendNotification(alarmMessages);

        // then
        verify(fcmRetryExecutor, times(1)).sendEach(anyList());
    }

    @Test
    @DisplayName("만료된 토큰(UNREGISTERED) 에러코드 반환 시 토큰 삭제 매니저 호출")
    void sendNotification_FAIL_DELETE() throws Exception {
        // given
        String expiredToken = "expiredToken";
        Message message = Message.builder().setToken(expiredToken).build();
        AlarmMessages alarmMessages = new AlarmMessages(List.of(message), List.of(expiredToken));

        BatchResponse batchResponse = mock(BatchResponse.class);
        SendResponse sendResponse = mock(SendResponse.class);
        FirebaseMessagingException exception = mock(FirebaseMessagingException.class);

        given(fcmRetryExecutor.sendEach(anyList())).willReturn(batchResponse);
        given(batchResponse.getResponses()).willReturn(List.of(sendResponse));
        given(sendResponse.isSuccessful()).willReturn(false);
        given(sendResponse.getException()).willReturn(exception);
        given(exception.getMessagingErrorCode()).willReturn(MessagingErrorCode.UNREGISTERED);

        // when
        fcmSender.sendNotification(alarmMessages);

        // then
        verify(fcmManager, times(1)).removeAll(List.of(expiredToken));
    }

    @Test
    @DisplayName("네트워크 오류(UNAVAILABLE) 에러코드 반환 시 재시도 sendEach 호출")
    void sendNotification_RETRY() throws Exception {
        // given
        String token = "retryToken";
        Message message = Message.builder().setToken(token).build();
        AlarmMessages alarmMessages = new AlarmMessages(List.of(message), List.of(token));

        BatchResponse batchResponse = mock(BatchResponse.class);
        SendResponse sendResponse = mock(SendResponse.class);
        FirebaseMessagingException exception = mock(FirebaseMessagingException.class);

        given(fcmRetryExecutor.sendEach(anyList())).willReturn(batchResponse);
        given(batchResponse.getResponses()).willReturn(List.of(sendResponse));
        given(sendResponse.isSuccessful()).willReturn(false);
        given(sendResponse.getException()).willReturn(exception);
        given(exception.getMessagingErrorCode()).willReturn(MessagingErrorCode.UNAVAILABLE);

        // when
        fcmSender.sendNotification(alarmMessages);

        // then
        verify(fcmRetryExecutor, times(2)).sendEach(anyList());
    }

    @Test
    @DisplayName("fcmRetryExecutor.sendEach 예외 발생 시 경고 로그만 출력하고 종료")
    void sendNotification_EXCEPTION() throws Exception {
        // given
        Message message = Message.builder().setToken("token").build();
        AlarmMessages alarmMessages = new AlarmMessages(List.of(message), List.of("token"));

        FirebaseMessagingException exception = mock(FirebaseMessagingException.class);
        given(fcmRetryExecutor.sendEach(anyList())).willThrow(exception);

        // when
        fcmSender.sendNotification(alarmMessages);

        // then
        verify(fcmManager, never()).removeAll(anyList());
        verify(fcmRetryExecutor, times(1)).sendEach(anyList());
    }
}
