package com.foodkeeper.foodkeeperserver.notification.implement;

import com.foodkeeper.foodkeeperserver.notification.dataaccess.repository.FcmRepository;
import com.foodkeeper.foodkeeperserver.notification.domain.AlarmMessages;
import com.google.firebase.messaging.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FcmSenderTest {

    @InjectMocks
    private FcmSender fcmSender;

    @Mock
    private FcmRepository fcmRepository;

    @BeforeEach
    void setUp() {
        FcmManager fcmManager = new FcmManager(fcmRepository);
        fcmSender = new FcmSender(fcmManager);
    }

    @Test
    @DisplayName("알림 전송 요청 시 FirebaseMessaging 다중 전송(sendEach) 호출 성공")
    void sendNotification_SUCCESS() throws Exception {
        try (MockedStatic<FirebaseMessaging> mockFirebase = mockStatic(FirebaseMessaging.class)) {
            // given
            FirebaseMessaging messaging = mock(FirebaseMessaging.class);
            mockFirebase.when(FirebaseMessaging::getInstance).thenReturn(messaging);

            Message message = Message.builder().setToken("validToken").build();
            AlarmMessages alarmMessages = new AlarmMessages(List.of(message), List.of("validToken"));

            BatchResponse batchResponse = mock(BatchResponse.class);
            SendResponse sendResponse = mock(SendResponse.class);

            given(messaging.sendEach(anyList(), eq(true))).willReturn(batchResponse);
            given(batchResponse.getResponses()).willReturn(List.of(sendResponse));
            given(sendResponse.isSuccessful()).willReturn(true);

            // when
            fcmSender.sendNotification(alarmMessages);

            // then
            verify(messaging, times(1)).sendEach(anyList(), eq(true));
        }
    }

    @Test
    @DisplayName("만료된 토큰(UNREGISTERED) 에러코드 반환 시 토큰 삭제 매니저 호출")
    void sendNotification_FAIL_DELETE() throws Exception {
        try (MockedStatic<FirebaseMessaging> mockFirebase = mockStatic(FirebaseMessaging.class)) {
            // given
            FirebaseMessaging messaging = mock(FirebaseMessaging.class);
            mockFirebase.when(FirebaseMessaging::getInstance).thenReturn(messaging);

            String expiredToken = "expiredToken";
            Message message = Message.builder().setToken(expiredToken).build();
            AlarmMessages alarmMessages = new AlarmMessages(List.of(message), List.of(expiredToken));

            BatchResponse batchResponse = mock(BatchResponse.class);
            SendResponse sendResponse = mock(SendResponse.class);
            FirebaseMessagingException exception = mock(FirebaseMessagingException.class);

            given(messaging.sendEach(anyList(), eq(true))).willReturn(batchResponse);
            given(batchResponse.getResponses()).willReturn(List.of(sendResponse));
            given(sendResponse.isSuccessful()).willReturn(false);
            given(sendResponse.getException()).willReturn(exception);
            given(exception.getMessagingErrorCode()).willReturn(MessagingErrorCode.UNREGISTERED);

            // when
            fcmSender.sendNotification(alarmMessages);

            // then
            verify(fcmRepository, times(1)).deleteAll(List.of(expiredToken));
        }
    }
}
