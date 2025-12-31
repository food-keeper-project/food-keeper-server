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

@Service
@RequiredArgsConstructor
@Slf4j
public class FoodNotificationService {

    private final FcmSender fcmSender;
    private final FoodManager foodManager;
    private final FcmManager fcmManager;

    public void sendExpiryAlarm(LocalDate today) {
        log.info("[Expiry Alarm] 유통기한 알림 전송 시작");

        List<Food> foods = foodManager.findFoodsToNotify(today);
        if (foods.isEmpty()) {
            log.info("[Expiry Alarm] 보낼 알림이 없습니다.");
            return;
        }

        List<String> memberKeys = foods.stream().map(Food::memberKey).distinct().toList();
        Map<String, List<String>> tokenMap = fcmManager.findTokens(memberKeys);


        foods.forEach(food -> {
            List<String> tokens = tokenMap.getOrDefault(food.memberKey(), Collections.emptyList());
            tokens.forEach(token -> {
                FcmMessage message = createFcmMessage(food, token, today);
                fcmSender.sendNotification(message);
            });
        });

        log.info("[Expiry Alarm] 전송 요청 완료. 대상 음식 수: {}", foods.size());
    }

    private FcmMessage createFcmMessage(Food food, String token, LocalDate today) {
        long remainDays = food.calculateRemainDay(today);
        String title = "유통기한 임박 알림";
        String body = String.format("%s의 유통기한이 %d일 남았습니다", food.name(), remainDays);
        return new FcmMessage(token, title, body);
    }
}
