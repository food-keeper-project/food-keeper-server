package com.foodkeeper.foodkeeperserver.notification.business;

import com.foodkeeper.foodkeeperserver.common.domain.Cursorable;
import com.foodkeeper.foodkeeperserver.common.domain.SliceObject;
import com.foodkeeper.foodkeeperserver.food.domain.Food;
import com.foodkeeper.foodkeeperserver.food.fixture.FoodFixture;
import com.foodkeeper.foodkeeperserver.food.implement.FoodReader;
import com.foodkeeper.foodkeeperserver.notification.domain.AlarmMessages;
import com.foodkeeper.foodkeeperserver.notification.domain.MemberFcmTokens;
import com.foodkeeper.foodkeeperserver.notification.implement.FcmManager;
import com.foodkeeper.foodkeeperserver.notification.implement.FcmSender;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class FoodNotificationServiceTest {

    @InjectMocks
    FoodNotificationService foodNotificationService;
    @Mock
    FoodReader foodReader;
    @Mock
    FcmManager fcmManager;
    @Mock
    FcmSender fcmSender;

    @Test
    @DisplayName("식재료가 1개일 때 AlarmMessages가 정상적으로 생성되어 전송 호출됨")
    void send_expiryAlarm_SUCCESS() {
        //given
        String memberKey = "memberKey";
        String token = "token";
        LocalDate today = LocalDate.now();
        Food food = FoodFixture.createFood(1L);

        given(foodReader.findFoodsToNotify(any(Cursorable.class), eq(today)))
                .willReturn(new SliceObject<>(List.of(food), new Cursorable<>(0, 1), false));

        MemberFcmTokens memberFcmTokens = new MemberFcmTokens(Map.of(memberKey, List.of(token)));
        given(fcmManager.findTokens(anySet())).willReturn(memberFcmTokens);

        // when
        foodNotificationService.sendExpiryAlarm();

        // then
        ArgumentCaptor<AlarmMessages> captor = ArgumentCaptor.forClass(AlarmMessages.class);
        verify(fcmSender).sendNotification(captor.capture());

        AlarmMessages capturedAlarmMessages = captor.getValue();

        assertThat(capturedAlarmMessages.tokens()).containsExactly(token);
        assertThat(capturedAlarmMessages.messages()).hasSize(1);
    }

    @Test
    @DisplayName("한 사용자의 식재료가 여러 건일 때 AlarmMessages가 정상적으로 생성되어 전송 호출됨")
    void sendMultipleAlarm_SUCCESS() {
        // given
        String memberKey = "memberKey";
        String token = "token";
        LocalDate today = LocalDate.now();

        Food food1 = FoodFixture.createFood(1L);
        Food food2 = FoodFixture.createFood(2L);

        given(foodReader.findFoodsToNotify(any(Cursorable.class), eq(today)))
                .willReturn(new SliceObject<>(List.of(food1, food2), new Cursorable<>(0, 50), false));

        MemberFcmTokens fcmTokens = new MemberFcmTokens(Map.of(memberKey, List.of(token)));
        given(fcmManager.findTokens(anySet())).willReturn(fcmTokens);

        // when
        foodNotificationService.sendExpiryAlarm();

        // then
        ArgumentCaptor<AlarmMessages> captor = ArgumentCaptor.forClass(AlarmMessages.class);
        verify(fcmSender).sendNotification(captor.capture());

        AlarmMessages capturedAlarmMessages = captor.getValue();
        assertThat(capturedAlarmMessages.tokens()).containsExactly(token);
        assertThat(capturedAlarmMessages.messages()).hasSize(1);
    }
}
