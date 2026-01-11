package com.foodkeeper.foodkeeperserver.auth.implement;

import com.foodkeeper.foodkeeperserver.auth.dataaccess.entity.LocalAuthEntity;
import com.foodkeeper.foodkeeperserver.auth.dataaccess.repository.LocalAuthRepository;
import com.foodkeeper.foodkeeperserver.auth.domain.LocalAccount;
import com.foodkeeper.foodkeeperserver.auth.domain.LocalAuth;
import com.foodkeeper.foodkeeperserver.auth.domain.Password;
import com.foodkeeper.foodkeeperserver.common.handler.TransactionHandler;
import com.foodkeeper.foodkeeperserver.mail.implement.AppMailSender;
import com.foodkeeper.foodkeeperserver.member.domain.Email;
import com.foodkeeper.foodkeeperserver.support.exception.AppException;
import com.foodkeeper.foodkeeperserver.support.exception.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class LocalAuthRecoverer {
    private static final String FIND_ACCOUNT_TITLE = "[키친로그] 아이디 찾기 결과";
    private static final String FIND_ACCOUNT_BODY = "고객님의 아이디는: %s 입니다.";

    private final LocalAuthRepository localAuthRepository;
    private final LocalAuthFinder localAuthFinder;
    private final AppMailSender mailSender;
    private final TransactionHandler transactionHandler;
    private final PasswordEncoder passwordEncoder;

    public void recoverAccount(Email email) {
        LocalAuth localAuth = localAuthFinder.findByEmail(email);

        transactionHandler.afterCommit(() ->
                mailSender.send(email.email(), FIND_ACCOUNT_TITLE, FIND_ACCOUNT_BODY.formatted(localAuth.getAccount())));
    }

    @Transactional
    public void changePassword(Email email, LocalAccount account, Password newPassword) {
        LocalAuthEntity localAuthEntity =
                localAuthRepository.findByEmailAndAccount(email.email(), account.account())
                        .orElseThrow(() -> new AppException(ErrorType.NOT_FOUND_ACCOUNT));

        localAuthEntity.updatePassword(passwordEncoder.encode(newPassword.password()));
    }
}
