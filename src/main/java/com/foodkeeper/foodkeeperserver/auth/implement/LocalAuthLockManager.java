package com.foodkeeper.foodkeeperserver.auth.implement;

import com.foodkeeper.foodkeeperserver.auth.dataaccess.repository.LocalAuthRepository;
import com.foodkeeper.foodkeeperserver.support.exception.AppException;
import com.foodkeeper.foodkeeperserver.support.exception.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LocalAuthLockManager {
    private final LocalAuthRepository localAuthRepository;

    public void acquire(String key, int timeout) {
        Integer result = localAuthRepository.getLock(key, timeout);
        if (result == null || result == 0) {
            throw new AppException(ErrorType.DEFAULT_ERROR);
        }
    }

    public void release(String key) {
        localAuthRepository.releaseLock(key);
    }
}
