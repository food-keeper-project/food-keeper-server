package com.foodkeeper.foodkeeperserver.mail.implement;

import com.foodkeeper.foodkeeperserver.support.exception.AppException;
import com.foodkeeper.foodkeeperserver.support.exception.ErrorType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppMailSender {
    private final JavaMailSender javaMailSender;

    @Async
    public void send(String email, String title, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();

            message.setTo(email);
            message.setSubject(title);
            message.setText(body);
            javaMailSender.send(message);
        } catch (MailException e) {
            log.error("[MAIL SEND FAILED]: to={} | cause={}", email, e.getMessage());
            throw new AppException(ErrorType.MAIL_SEND_FAILED);
        }
    }
}
