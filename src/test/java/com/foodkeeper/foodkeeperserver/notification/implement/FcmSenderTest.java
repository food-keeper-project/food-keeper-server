package com.foodkeeper.foodkeeperserver.notification.implement;

import com.foodkeeper.foodkeeperserver.notification.dataaccess.repository.FcmRepository;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.MessagingErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
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
    @DisplayName("알림 전송 요청 시 FirebaseMessaging 호출 성공")
    void sendNotification_SUCCESS() throws Exception {
        try (MockedStatic<FirebaseMessaging> mockFirebase = mockStatic(FirebaseMessaging.class)) {
            // given
            Map<String, String> data = new HashMap<>();

            FirebaseMessaging messaging = mock(FirebaseMessaging.class);
            mockFirebase.when(FirebaseMessaging::getInstance).thenReturn(messaging);

            // when
            fcmSender.sendNotification("fcmToken", data);

            // then
            verify(messaging, times(1)).send(any(Message.class));
            verify(fcmRepository, never()).deleteByToken(anyString());
        }
    }

    @Test
    @DisplayName("만료된 토큰 에러코드 반환 시 토큰 삭제 성공")
    void sendNotification_FAIL_DELETE() throws Exception {
        try (MockedStatic<FirebaseMessaging> mockFirebase = mockStatic(FirebaseMessaging.class)) {
            String expiredToken = "token";
            Map<String, String> data = new HashMap<>();

            FirebaseMessaging messaging = mock(FirebaseMessaging.class);
            mockFirebase.when(FirebaseMessaging::getInstance).thenReturn(messaging);

            FirebaseMessagingException exception = mock(FirebaseMessagingException.class);
            given(exception.getMessagingErrorCode()).willReturn(MessagingErrorCode.UNREGISTERED);
            doThrow(exception).when(messaging).send(any(Message.class));

            //when
            fcmSender.sendNotification(expiredToken, data);
            //then
            verify(fcmRepository, times(1)).deleteByToken(expiredToken);

        }


    }
}
