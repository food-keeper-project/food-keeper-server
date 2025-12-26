package com.foodkeeper.foodkeeperserver.notification.implement;

import com.foodkeeper.foodkeeperserver.food.domain.Food;
import com.foodkeeper.foodkeeperserver.food.implement.FoodManager;
import com.foodkeeper.foodkeeperserver.notification.domain.FcmSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class Notifier {

    private final FoodManager foodManager;
    private final FcmManager fcmManager;

    public List<FcmSender> sendFoodNotification(LocalDate today) {
        List<Food> foods = foodManager.findFoodsToNotify(today);
        List<String> memberKeys = foods.stream()
                .map(Food::memberKey)
                .distinct()
                .toList();

        Map<String, List<String>> tokenMap = fcmManager.findTokens(memberKeys);

        return foods.stream()
                .filter(food -> tokenMap.containsKey(food.memberKey()))
                .flatMap(food -> {
                    List<String> tokens = tokenMap.get(food.memberKey());
                    return tokens.stream()
                            .map(token -> createFcmSender(food, token, today));
                })
                .toList();
    }

    private FcmSender createFcmSender(Food food, String token, LocalDate today) {
        long remainDays = food.calculateRemainDay(today);
        String title = "유통기한 임박 알림";
        String body = String.format("%s의 유통기한이 %d일 남았습니다", food.name(), remainDays);
        return new FcmSender(token, title, body);
    }
}
