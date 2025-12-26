package com.foodkeeper.foodkeeperserver.notification.implement;

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
        List<FcmTokenEntity> tokens = fcmRepository.findAllByMemberKeyIn(memberKeys);
        return tokens.stream()
                .map(FcmTokenEntity::toDomain)
                .collect(Collectors.groupingBy(
                        FcmToken::memberKey,
                        Collectors.mapping(FcmToken::fcmToken, Collectors.toList())
                ));
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
    // hard delete : firebase 에서 만료된 토큰은 굳이 soft delete 필요없을 것 같음
    @Transactional
    public void remove(String fcmToken) {
        fcmRepository.deleteByToken(fcmToken);
    }
}
