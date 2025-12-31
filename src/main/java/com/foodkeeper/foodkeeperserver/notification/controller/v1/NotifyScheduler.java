package com.foodkeeper.foodkeeperserver.notification.controller.v1;

import com.foodkeeper.foodkeeperserver.notification.business.FoodNotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
@Service
public class NotifyScheduler {

    private final FoodNotificationService foodNotificationService;

    @Scheduled(cron = "0 0 12 * * *")
    public void scheduleExpiryAlarm() {
        foodNotificationService.sendExpiryAlarm(LocalDate.now());
    }
}
