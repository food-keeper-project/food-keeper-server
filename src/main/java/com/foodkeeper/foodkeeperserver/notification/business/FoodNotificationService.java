package com.foodkeeper.foodkeeperserver.notification.business;

import com.foodkeeper.foodkeeperserver.food.domain.Food;
import com.foodkeeper.foodkeeperserver.food.implement.FoodManager;
import com.foodkeeper.foodkeeperserver.notification.domain.FcmMessage;
import com.foodkeeper.foodkeeperserver.notification.implement.FcmManager;
import com.foodkeeper.foodkeeperserver.notification.implement.FcmSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FoodNotificationService {

    private final FcmSender fcmSender;
    private final FoodManager foodManager;
    private final FcmManager fcmManager;
    private final String TITLE = "유통기한 임박 알림";

    public void sendExpiryAlarm(LocalDate today) {
        log.info("[Expiry Alarm] 유통기한 알림 전송 시작");

        List<Food> foods = foodManager.findFoodsToNotify(today);
        if (foods.isEmpty()) {
            log.info("[Expiry Alarm] 보낼 알림이 없습니다.");
            return;
        }

        Map<String, List<Food>> foodsByMember = foods.stream()
                .collect(Collectors.groupingBy(Food::memberKey));

        Map<String, List<String>> tokenMap = fcmManager.findTokens(foodsByMember.keySet());

        foodsByMember.forEach((memberKey, memberFoods) -> {
            List<String> tokens = tokenMap.getOrDefault(memberKey, Collections.emptyList());

            if (tokens.isEmpty()) return;
            FcmMessage message = createFcmMessage(memberFoods, tokens.get(0), today);

            tokens.forEach(token -> {
                FcmMessage tokenMessage = new FcmMessage(token, message.title(), message.body());
                fcmSender.sendNotification(tokenMessage);
            });
        });

        log.info("[Expiry Alarm] 전송 요청 완료. 대상 음식 수: {}", foods.size());
    }

    private FcmMessage createFcmMessage(List<Food> foods, String token, LocalDate today) {
        String title = TITLE;
        String body;
        Food firstFood = foods.get(0);
        long remainDays = firstFood.calculateRemainDay(today);

        if (foods.size() == 1) {
            body = String.format("%s의 유통기한이 %d일 남았습니다", firstFood.name(), remainDays);
        } else {
            body = String.format("%s 외 %d건의 유통기한이 임박했습니다", firstFood.name(), foods.size() - 1);
        }
        return new FcmMessage(token, title, body);
    }
}
