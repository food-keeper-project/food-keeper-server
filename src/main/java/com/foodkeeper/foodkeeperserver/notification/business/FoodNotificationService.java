package com.foodkeeper.foodkeeperserver.notification.business;

import com.foodkeeper.foodkeeperserver.food.domain.Food;
import com.foodkeeper.foodkeeperserver.food.implement.FoodReader;
import com.foodkeeper.foodkeeperserver.notification.domain.FcmMessage;
import com.foodkeeper.foodkeeperserver.notification.domain.MemberFcmTokens;
import com.foodkeeper.foodkeeperserver.notification.implement.FcmManager;
import com.foodkeeper.foodkeeperserver.notification.implement.FcmSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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

    @Scheduled(cron = "0 0 12 * * *")
    public void sendExpiryAlarm() {
        LocalDate today = LocalDate.now();
        log.info("[Expiry Alarm] 유통기한 알림 전송 시작");

        List<Food> foods = foodReader.findFoodsToNotify(today);
        if (foods.isEmpty()) {
            log.info("[Expiry Alarm] 보낼 알림이 없습니다.");
            return;
        }

        Map<String, List<Food>> foodsByMember = foods.stream()
                .collect(Collectors.groupingBy(Food::memberKey));

        MemberFcmTokens memberFcmTokens = fcmManager.findTokens(foodsByMember.keySet());

        foodsByMember.forEach((memberKey, memberFoods) -> {
            if (!memberFcmTokens.hasTokens(memberKey)) return;
            List<String> tokens = memberFcmTokens.getTokensByMember(memberKey);

            FcmMessage message = createFcmMessage(memberFoods, tokens.getFirst(), today);

            tokens.forEach(token -> {
                FcmMessage tokenMessage = new FcmMessage(token, message.title(), message.body());
                fcmSender.sendNotification(tokenMessage);
            });
        });

        log.info("[Expiry Alarm] 전송 요청 완료. 대상 음식 수: {}", foods.size());
    }

    private FcmMessage createFcmMessage(List<Food> foods, String token, LocalDate today) {
        String body;
        String title;
        Food firstFood = foods.getFirst();
        long remainDays = firstFood.calculateRemainDay(today);

        if (foods.size() == 1) {
            title = "%s D-%d".formatted(firstFood.name(), remainDays);
            body = "%s 유통기한까지 %d일 남았어요.\n낭비되지 않도록 미리 확인해보세요!".formatted(firstFood.name(), remainDays);
        } else {
            title = "%s 외 %d건".formatted(firstFood.name(), foods.size() - 1);
            body = "%s 외 %d건의 식품이 곧 유통기한이 임박 예정이에요!".formatted(firstFood.name(), foods.size() - 1);
        }
        return new FcmMessage(token, title, body);
    }
}
