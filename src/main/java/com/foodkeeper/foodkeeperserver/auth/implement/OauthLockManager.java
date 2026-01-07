package com.foodkeeper.foodkeeperserver.auth.implement;

import com.foodkeeper.foodkeeperserver.auth.dataaccess.repository.OauthRepository;
import com.foodkeeper.foodkeeperserver.support.exception.AppException;
import com.foodkeeper.foodkeeperserver.support.exception.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OauthLockManager {
    private final OauthRepository oauthRepository;

    public void acquire(String key, int timeout) {
        Integer result = oauthRepository.getLock(key, timeout);
        if (result == null || result == 0) {
            throw new AppException(ErrorType.DEFAULT_ERROR);
        }
    }

    public void release(String key) {
        oauthRepository.releaseLock(key);
    }
}
