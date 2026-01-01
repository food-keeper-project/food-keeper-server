package com.foodkeeper.foodkeeperserver.notification.implement;

import com.foodkeeper.foodkeeperserver.notification.dataaccess.entity.FcmTokenEntity;
import com.foodkeeper.foodkeeperserver.notification.dataaccess.repository.FcmRepository;
import com.foodkeeper.foodkeeperserver.notification.domain.MemberFcmTokens;
import com.foodkeeper.foodkeeperserver.notification.fixture.FcmFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class FcmManagerTest {

    @InjectMocks FcmManager fcmManager;
    @Mock FcmRepository fcmRepository;

    @Test
    @DisplayName("memberKey 리스트에 매핑되는 fcm 토큰 리스트 조회 후 Map 에 저장")
    void findTokens_SUCCESS() throws Exception {
        // given
        Set<String> memberKeys = Set.of("key1", "key2");
        List<FcmTokenEntity> tokenEntities = List.of(
                FcmFixture.createEntity(1L, "token1", "key1"),
                FcmFixture.createEntity(2L, "token2", "key1"),
                FcmFixture.createEntity(3L, "token3", "key2")
        );
        given(fcmRepository.findAllByMemberKeyIn(memberKeys)).willReturn(tokenEntities);

        // when
        MemberFcmTokens result = fcmManager.findTokens(memberKeys);

        // then
        assertThat(result).isNotNull();

        List<String> key1Tokens = result.getTokensByMember("key1");
        assertThat(key1Tokens).hasSize(2)
                .containsExactlyInAnyOrder("token1", "token2");
        List<String> key2Tokens = result.getTokensByMember("key2");
        assertThat(key2Tokens).hasSize(1)
                .containsExactly("token3");
        assertThat(result.getTokensByMember("key3")).isEmpty();
    }

    @Test
    @DisplayName("fcm 토큰은 존재하지만 memberKey 가 다를 때 memberKey UPDATE")
    void updateMemberKey_SUCCESS() throws Exception {
        //given
        String token = "tok";
        String reqMemberKey = "valid-member";
        String dbMemberKey = "member1";
        FcmTokenEntity fcmTokenEntity = FcmFixture.createEntity(1L, token, dbMemberKey);
        given(fcmRepository.findByToken(token)).willReturn(Optional.ofNullable(fcmTokenEntity));
        //when
        fcmManager.addTokenOrUpdate(token, reqMemberKey);
        //then
        assertThat(fcmTokenEntity.getMemberKey()).isEqualTo(reqMemberKey);
    }

    @Test
    @DisplayName("fcm 토큰이 존재하지 않을 때 새로운 fcm 토큰 생성")
    void addToken_SUCCESS() throws Exception {
        //given
        String token = "tok";
        String memberKey = "memberKey";
        FcmTokenEntity entity = FcmFixture.createEntity(1L, token, memberKey);
        given(fcmRepository.findByToken(token)).willReturn(Optional.empty());
        given(fcmRepository.save(any(FcmTokenEntity.class))).willReturn(entity);
        //when
        fcmManager.addTokenOrUpdate(token, memberKey);
        //then
        ArgumentCaptor<FcmTokenEntity> captor = ArgumentCaptor.forClass(FcmTokenEntity.class);
        verify(fcmRepository).save(captor.capture());
        FcmTokenEntity fcm = captor.getValue();
        assertThat(fcm.getToken()).isEqualTo(token);
        assertThat(fcm.getMemberKey()).isEqualTo(memberKey);
    }

    @Test
    @DisplayName("토큰 hard delete")
    void remove_SUCCESS() throws Exception {
        //given
        String token = "token";
        willDoNothing().given(fcmRepository).deleteByToken(token);
        //when
        fcmManager.remove(token);
        //then
        verify(fcmRepository, times(1)).deleteByToken(token);
    }
}
