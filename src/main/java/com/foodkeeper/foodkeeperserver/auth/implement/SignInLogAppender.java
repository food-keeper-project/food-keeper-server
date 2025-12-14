package com.foodkeeper.foodkeeperserver.auth.implement;

import com.foodkeeper.foodkeeperserver.auth.dataaccess.entity.SignInLogEntity;
import com.foodkeeper.foodkeeperserver.auth.dataaccess.repository.SignInLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class SignInLogAppender {
    private final SignInLogRepository signInLogRepository;

    @Transactional
    public Long append(String ipAddress, String memberKey) {
        return signInLogRepository.save(new SignInLogEntity(ipAddress, memberKey)).getId();
    }
}
