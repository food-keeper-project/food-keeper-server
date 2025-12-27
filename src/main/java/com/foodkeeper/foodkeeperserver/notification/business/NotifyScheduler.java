package com.foodkeeper.foodkeeperserver.notification.business;

import com.foodkeeper.foodkeeperserver.notification.domain.FcmMessage;
import com.foodkeeper.foodkeeperserver.notification.implement.Notifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class NotifyScheduler {

    private final NotifyService notifyService;
    private final Notifier notifier;

    @Scheduled(cron = "0 0 12 * * *")
    public void scheduleExpiryAlarm() {
        log.info("[Expiry Alarm] 유통기한 알림 전송 시작");
        List<FcmMessage> senders = notifier.sendFoodNotification(LocalDate.now());

        if (senders.isEmpty()) {
            log.info("[Expiry Alarm] 보낼 알림이 없습니다.");
            return;
        }
        for (FcmMessage sender : senders) {
            notifyService.sendNotification(sender);
        }
    }

}
