package com.foodkeeper.foodkeeperserver.auth.business;

import com.foodkeeper.foodkeeperserver.auth.domain.SignInEvent;
import com.foodkeeper.foodkeeperserver.auth.implement.RefreshTokenManager;
import com.foodkeeper.foodkeeperserver.auth.implement.SignInLogAppender;
import com.foodkeeper.foodkeeperserver.notification.implement.FcmManager;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SignInEventListener {
    private final RefreshTokenManager refreshTokenManager;
    private final SignInLogAppender signInLogAppender;
    private final FcmManager fcmManager;

    @EventListener
    public void handleSignInEvent(SignInEvent event) {
        signInLogAppender.append(event.ipAddress(), event.memberKey());

        refreshTokenManager.updateRefreshToken(event.memberKey(), event.refreshToken());
        fcmManager.addTokenOrUpdate(event.fcmToken(), event.memberKey());
    }
}
