package com.foodkeeper.foodkeeperserver.notification.implement;

import com.foodkeeper.foodkeeperserver.common.utils.ListUtil;
import com.foodkeeper.foodkeeperserver.notification.dataaccess.entity.FcmTokenEntity;
import com.foodkeeper.foodkeeperserver.notification.dataaccess.repository.FcmRepository;
import com.foodkeeper.foodkeeperserver.notification.domain.FcmToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class FcmManager {

    private final FcmRepository fcmRepository;

    public Map<String, List<String>> findTokens(List<String> memberKeys) {
        List<FcmTokenEntity> tokens = ListUtil.getOrElseThrowList(fcmRepository.findAllByMemberKeyIn(memberKeys));
        return tokens.stream()
                .map(FcmTokenEntity::toDomain)
                .collect(Collectors.groupingBy(
                        FcmToken::memberKey,
                        Collectors.mapping(FcmToken::token, Collectors.toList())
                ));
    }

    @Transactional
    public void addTokenOrUpdate(String token, String memberKey) {
        fcmRepository.findByToken(token)
                .ifPresentOrElse(
                        fcmTokenEntity -> {
                            if (!fcmTokenEntity.getMemberKey().equals(memberKey)) {
                                fcmTokenEntity.changeMemberKey(memberKey);
                            }
                            fcmTokenEntity.update();
                        }, () -> {
                            FcmToken newToken = FcmToken.create(token, memberKey);
                            fcmRepository.save(FcmTokenEntity.from(newToken));
                        }
                );
    }

    @Transactional
    public void remove(String token) {
        fcmRepository.deleteByToken(token);
    }
}
