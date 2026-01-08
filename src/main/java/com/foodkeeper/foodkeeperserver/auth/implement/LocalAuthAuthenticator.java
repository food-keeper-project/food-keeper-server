package com.foodkeeper.foodkeeperserver.auth.implement;

import com.foodkeeper.foodkeeperserver.auth.dataaccess.repository.LocalAuthRepository;
import com.foodkeeper.foodkeeperserver.auth.domain.LocalAccount;
import com.foodkeeper.foodkeeperserver.auth.domain.Password;
import com.foodkeeper.foodkeeperserver.support.exception.AppException;
import com.foodkeeper.foodkeeperserver.support.exception.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LocalAuthAuthenticator {

    private final LocalAuthRepository localAuthRepository;
    private final PasswordEncoder passwordEncoder;

    public boolean isDuplicatedAccount(String account) {
        return localAuthRepository.existsByAccount(account);
    }

    public String encodePassword(Password password) {
        return passwordEncoder.encode(password.password());
    }

    /** @return memberKey */
    public String authenticate(LocalAccount account, Password password) {
        String encodedPassword = encodePassword(password);
        return localAuthRepository.findByAccountAndPassword(account.account(), encodedPassword)
                .orElseThrow(() -> new AppException(ErrorType.NOT_FOUND_ACCOUNT))
                .getMemberKey();
    }
}
