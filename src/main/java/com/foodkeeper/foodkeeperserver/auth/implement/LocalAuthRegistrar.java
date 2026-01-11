package com.foodkeeper.foodkeeperserver.auth.implement;

import com.foodkeeper.foodkeeperserver.auth.dataaccess.entity.LocalAuthEntity;
import com.foodkeeper.foodkeeperserver.auth.dataaccess.repository.LocalAuthRepository;
import com.foodkeeper.foodkeeperserver.auth.domain.NewLocalMember;
import com.foodkeeper.foodkeeperserver.member.implement.MemberRegistrar;
import com.foodkeeper.foodkeeperserver.support.exception.AppException;
import com.foodkeeper.foodkeeperserver.support.exception.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class LocalAuthRegistrar {

    private final LocalAuthRepository localAuthRepository;
    private final MemberRegistrar memberRegistrar;

    @Transactional
    public void register(NewLocalMember newLocalMember) {
        if (localAuthRepository.existsByEmail(newLocalMember.member().getEmail())) {
            throw new AppException(ErrorType.INVALID_EMAIL);
        }

        String memberKey = memberRegistrar.register(newLocalMember.member());
        localAuthRepository.save(
                new LocalAuthEntity(newLocalMember.getAccount(), newLocalMember.getPassword(), memberKey));
    }
}
