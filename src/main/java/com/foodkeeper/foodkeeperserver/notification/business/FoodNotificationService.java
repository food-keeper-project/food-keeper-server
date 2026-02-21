package com.foodkeeper.foodkeeperserver.notification.business;

import com.foodkeeper.foodkeeperserver.common.domain.Cursorable;
import com.foodkeeper.foodkeeperserver.common.domain.SliceObject;
import com.foodkeeper.foodkeeperserver.food.domain.Food;
import com.foodkeeper.foodkeeperserver.food.implement.FoodReader;
import com.foodkeeper.foodkeeperserver.notification.domain.MemberFcmTokens;
import com.foodkeeper.foodkeeperserver.notification.implement.FcmManager;
import com.foodkeeper.foodkeeperserver.notification.implement.FcmSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FoodNotificationService {

    private final FcmSender fcmSender;
    private final FoodReader foodReader;
    private final FcmManager fcmManager;
    private static final String type = "EXPIRATION";
    private static final int limit = 500;

    @Scheduled(cron = "0 0 12 * * *")
    public void sendExpiryAlarm() {
        LocalDate today = LocalDate.now();
        log.info("[Expiry Alarm] 유통기한 알림 전송 시작");

        Long currentCursor = 0L;
        boolean hasNext = true;

        while(hasNext) {
            SliceObject<Food> foods = foodReader.findFoodsToNotify(new Cursorable<>(currentCursor, limit),today);
            List<Food> alarmFoods = foods.content();

            if (alarmFoods.isEmpty()) {
                log.info("[Expiry Alarm] 보낼 알림이 없습니다.");
                return;
            }

            Map<String, List<Food>> foodsByMember = alarmFoods.stream()
                    .collect(Collectors.groupingBy(Food::memberKey));

            MemberFcmTokens memberFcmTokens = fcmManager.findTokens(foodsByMember.keySet());
            foodsByMember.forEach((memberKey, memberFoods) -> {
                if (!memberFcmTokens.hasTokens(memberKey)) {
                    return;
                }

                Map<String, String> fcmMessage = createFcmMessage(memberFoods, today);
                memberFcmTokens.getTokensByMember(memberKey)
                        .forEach(token -> fcmSender.sendNotification(token, fcmMessage));
            });

            hasNext = foods.hasNext();
            if (hasNext) {
                Food lastFood = alarmFoods.get(alarmFoods.size() - 1);
                currentCursor = lastFood.id();
            }

            log.info("[Expiry Alarm] 전송 요청 완료. 대상 음식 수: {}", alarmFoods.size());
        }



    }

    private Map<String, String> createFcmMessage(List<Food> foods, LocalDate today) {
        Map<String, String> data = new HashMap<>();
        String title;
        Food firstFood = foods.getFirst();
        long remainDays = firstFood.calculateRemainDay(today);

        if (foods.size() == 1) {
            title = "%s D-%d".formatted(firstFood.name(), remainDays);
        } else {
            title = "%s 외 %d건".formatted(firstFood.name(), foods.size() - 1);
        }

        data.put("foodName", firstFood.name());
        data.put("remainingDays", String.valueOf(remainDays));
        data.put("title", title);
        data.put("type", type);

        return data;
    }
}
