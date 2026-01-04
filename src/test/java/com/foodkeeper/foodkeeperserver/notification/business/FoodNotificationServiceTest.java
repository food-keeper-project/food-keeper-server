package com.foodkeeper.foodkeeperserver.notification.business;

import com.foodkeeper.foodkeeperserver.food.domain.Food;
import com.foodkeeper.foodkeeperserver.food.fixture.FoodFixture;
import com.foodkeeper.foodkeeperserver.food.implement.FoodReader;
import com.foodkeeper.foodkeeperserver.notification.domain.FcmMessage;
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
import static org.mockito.ArgumentMatchers.anySet;
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
    @DisplayName("식재료가 1개일 떄 단일 메시지 알림 전송")
    void send_expiryAlarm_SUCCESS() {
        //given
        String memberKey = "memberKey";
        String token = "token";
        LocalDate today = LocalDate.now();
        Food food = FoodFixture.createFood(1L);
        given(foodReader.findFoodsToNotify(today)).willReturn(List.of(food));

        MemberFcmTokens memberFcmTokens = new MemberFcmTokens(Map.of(memberKey, List.of(token)));
        given(fcmManager.findTokens(anySet())).willReturn(memberFcmTokens);
        //when
        foodNotificationService.sendExpiryAlarm();
        //then
        ArgumentCaptor<FcmMessage> captor = ArgumentCaptor.forClass(FcmMessage.class);
        verify(fcmSender).sendNotification(captor.capture());

        assertThat(captor.getValue().title()).isEqualTo("우유 D-1");
        assertThat(captor.getValue().fcmToken()).isEqualTo(token);
    }

    @Test
    @DisplayName("한 사용자의 식재료가 여러 건 메시지 알림 전송")
    void sendMultipleAlarm_SUCCESS() {
        // given
        String memberKey = "memberKey";
        String token = "token";
        LocalDate today = LocalDate.now();
        // 같은 사용자
        Food food1 = FoodFixture.createFood(1L);
        Food food2 = FoodFixture.createFood(2L);

        given(foodReader.findFoodsToNotify(today)).willReturn(List.of(food1, food2));

        MemberFcmTokens fcmTokens = new MemberFcmTokens(Map.of(memberKey, List.of(token)));
        given(fcmManager.findTokens(anySet())).willReturn(fcmTokens);

        // when
        foodNotificationService.sendExpiryAlarm();

        ArgumentCaptor<FcmMessage> captor = ArgumentCaptor.forClass(FcmMessage.class);
        verify(fcmSender).sendNotification(captor.capture());
        assertThat(captor.getValue().title()).isEqualTo("우유 외 1건");
    }
}
