package com.foodkeeper.foodkeeperserver.notification.implement;

import com.foodkeeper.foodkeeperserver.notification.dataaccess.entity.FcmTokenEntity;
import com.foodkeeper.foodkeeperserver.notification.dataaccess.repository.FcmRepository;
import com.foodkeeper.foodkeeperserver.notification.domain.FcmToken;
import com.foodkeeper.foodkeeperserver.notification.domain.MemberFcmTokens;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FcmManager {

    private final FcmRepository fcmRepository;

    public MemberFcmTokens findTokens(Set<String> memberKeys) {
        List<FcmTokenEntity> tokens = fcmRepository.findAllByMemberKeyIn(memberKeys);
        Map<String, List<String>> groupedTokens = tokens.stream()
                .map(FcmTokenEntity::toDomain)
                .collect(Collectors.groupingBy(
                        FcmToken::memberKey,
                        Collectors.mapping(FcmToken::fcmToken, Collectors.toList())
                ));
        return new MemberFcmTokens(groupedTokens);
    }

    @Transactional
    public void addTokenOrUpdate(String fcmToken, String memberKey) {
        fcmRepository.findByToken(fcmToken)
                .ifPresentOrElse(
                        fcmTokenEntity -> {
                            if (!fcmTokenEntity.getMemberKey().equals(memberKey)) {
                                fcmTokenEntity.changeMemberKey(memberKey);
                            }
                            fcmTokenEntity.update();
                        }, () -> {
                            FcmToken newToken = FcmToken.create(fcmToken, memberKey);
                            fcmRepository.save(FcmTokenEntity.from(newToken));
                        }
                );
    }

    @Transactional
    public void remove(String fcmToken) {
        fcmRepository.deleteByToken(fcmToken);
    }
}
