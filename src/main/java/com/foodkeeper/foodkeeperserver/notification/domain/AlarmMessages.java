package com.foodkeeper.foodkeeperserver.notification.domain;

import com.foodkeeper.foodkeeperserver.food.domain.Food;
import com.google.firebase.messaging.AndroidConfig;
import com.google.firebase.messaging.Message;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public record AlarmMessages(List<Message> messages, List<String> tokens) {
    private static final String type = "EXPIRATION";

    public AlarmMessages(List<Message> messages, List<String> tokens) {
        this.messages = List.copyOf(messages);
        this.tokens = List.copyOf(tokens);
    }

    public static AlarmMessages create(Map<String, List<Food>> foodsByMember,
                                       MemberFcmTokens memberFcmTokens,
                                       LocalDate today) {
        List<Message> messages = new ArrayList<>();
        List<String> tokens = new ArrayList<>();

        AndroidConfig androidConfig = AndroidConfig.builder()
                .setPriority(AndroidConfig.Priority.HIGH)
                .build();

        foodsByMember.forEach((memberKey, memberFoods) -> {
            if (!memberFcmTokens.hasTokens(memberKey)) {
                return;
            }

            Map<String, String> fcmData = createAlarmMessage(memberFoods, today);

            memberFcmTokens.getTokensByMember(memberKey).forEach(token -> {
                messages.add(Message.builder()
                        .setToken(token)
                        .putAllData(fcmData)
                        .setAndroidConfig(androidConfig)
                        .build());
                tokens.add(token);
            });
        });

        return new AlarmMessages(messages, tokens);
    }


    private static Map<String, String> createAlarmMessage(List<Food> foods, LocalDate today) {
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

