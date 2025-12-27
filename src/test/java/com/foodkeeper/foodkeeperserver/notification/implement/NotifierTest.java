package com.foodkeeper.foodkeeperserver.notification.implement;

import com.foodkeeper.foodkeeperserver.food.domain.Food;
import com.foodkeeper.foodkeeperserver.food.fixture.FoodFixture;
import com.foodkeeper.foodkeeperserver.food.implement.FoodManager;
import com.foodkeeper.foodkeeperserver.notification.domain.FcmSender;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class NotifierTest {

    @InjectMocks Notifier notifier;
    @Mock FoodManager foodManager;
    @Mock FcmManager fcmManager;


    @Test
    @DisplayName("fcmSender 객체 생성")
    void sendNotification_SUCCESS() throws Exception {
        //given
        LocalDate today = LocalDate.of(2025,12,26);
        String memberKey = "memberKey";
        List<Food> foodList = List.of(
                FoodFixture.createFood(1L),
                FoodFixture.createFood(2L)
        );
        List<String> tokens = List.of("phone","computer");
        given(foodManager.findFoodsToNotify(today)).willReturn(foodList);
        given(fcmManager.findTokens(List.of(memberKey))).willReturn(Map.of(memberKey,tokens));

        //when
        List<FcmSender> result = notifier.sendFoodNotification(today);
        //then
        assertThat(result.getFirst().fcmToken()).isEqualTo("phone");
        assertThat(result.getFirst().title()).isEqualTo("유통기한 임박 알림");
        assertThat(result.getFirst().body()).contains("우유","1");
    }
}
