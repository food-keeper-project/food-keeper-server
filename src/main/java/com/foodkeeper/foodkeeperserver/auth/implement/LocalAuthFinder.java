package com.foodkeeper.foodkeeperserver.auth.implement;

import com.foodkeeper.foodkeeperserver.auth.dataaccess.repository.LocalAuthRepository;
import com.foodkeeper.foodkeeperserver.member.domain.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LocalAuthFinder {
    private final LocalAuthRepository localAuthRepository;

    public boolean existsEmail(Email email) {
        return localAuthRepository.existsEmail(email.email());
    }
}
