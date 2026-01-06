package com.foodkeeper.foodkeeperserver.auth.implement;

import com.foodkeeper.foodkeeperserver.auth.dataaccess.entity.OauthEntity;
import com.foodkeeper.foodkeeperserver.auth.dataaccess.repository.OauthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OauthFinder {
    private final OauthRepository oauthRepository;

    public Optional<String> findMemberKeyByOAuthAccount(String oAuthAccount) {
        return oauthRepository.findByAccount(oAuthAccount).map(OauthEntity::getMemberKey);
    }
}
