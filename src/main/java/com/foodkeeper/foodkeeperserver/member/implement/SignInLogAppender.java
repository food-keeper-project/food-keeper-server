package com.foodkeeper.foodkeeperserver.member.implement;

import com.foodkeeper.foodkeeperserver.member.dataaccess.entity.SignInLogEntity;
import com.foodkeeper.foodkeeperserver.member.dataaccess.repository.SignInLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SignInLogAppender {
    private final SignInLogRepository signInLogRepository;

    public Long append(String ipAddress, String memberKey) {
        return signInLogRepository.save(new SignInLogEntity(ipAddress, memberKey)).getId();
    }
}
