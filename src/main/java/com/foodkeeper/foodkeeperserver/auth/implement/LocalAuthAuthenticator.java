package com.foodkeeper.foodkeeperserver.auth.implement;

import com.foodkeeper.foodkeeperserver.auth.dataaccess.entity.LocalAuthEntity;
import com.foodkeeper.foodkeeperserver.auth.dataaccess.repository.LocalAuthRepository;
import com.foodkeeper.foodkeeperserver.auth.domain.EncodedPassword;
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

    public boolean isDuplicatedAccount(LocalAccount account) {
        return localAuthRepository.existsByAccount(account.account());
    }

    public EncodedPassword encodePassword(Password password) {
        return new EncodedPassword(passwordEncoder.encode(password.password()));
    }

    /** @return memberKey */
    public String authenticate(LocalAccount account, Password password) {
        LocalAuthEntity localAuthEntity = localAuthRepository.findByAccount(account.account())
                .orElseThrow(() -> new AppException(ErrorType.NOT_FOUND_ACCOUNT));

        if (!passwordEncoder.matches(password.password(), localAuthEntity.getPassword())) {
            throw new AppException(ErrorType.INVALID_PASSWORD);
        }

        return localAuthEntity.getMemberKey();
    }
}
