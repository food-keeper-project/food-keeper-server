package com.foodkeeper.foodkeeperserver.auth.implement;

import com.foodkeeper.foodkeeperserver.auth.dataaccess.repository.LocalAuthRepository;
import com.foodkeeper.foodkeeperserver.auth.domain.LocalAccount;
import com.foodkeeper.foodkeeperserver.auth.domain.LocalAuth;
import com.foodkeeper.foodkeeperserver.member.domain.Email;
import com.foodkeeper.foodkeeperserver.support.exception.AppException;
import com.foodkeeper.foodkeeperserver.support.exception.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LocalAuthFinder {
    private final LocalAuthRepository localAuthRepository;

    public boolean existsEmail(Email email) {
        return localAuthRepository.existsByEmail(email.email());
    }

    public boolean existsEmailAndAccount(Email email, LocalAccount account) {
        return localAuthRepository.existsByEmailAndAccount(email.email(), account.account());
    }

    public LocalAuth findByEmail(Email email) {
        return localAuthRepository.findByEmail(email.email())
                .orElseThrow(() -> new AppException(ErrorType.INVALID_EMAIL))
                .toDomain();
    }
}
