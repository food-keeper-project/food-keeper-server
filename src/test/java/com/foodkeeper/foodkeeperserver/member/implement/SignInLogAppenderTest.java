package com.foodkeeper.foodkeeperserver.member.implement;

import com.foodkeeper.foodkeeperserver.member.dataaccess.entity.SignInLogEntity;
import com.foodkeeper.foodkeeperserver.member.dataaccess.repository.SignInLogRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.spy;

@ExtendWith(MockitoExtension.class)
class SignInLogAppenderTest {

    @Mock SignInLogRepository signInLogRepository;
    SignInLogAppender signInLogAppender;

    @BeforeEach
    void setUp() {
        signInLogAppender = new SignInLogAppender(signInLogRepository);
    }

    @Test
    @DisplayName("로그인 정보를 기록한다.")
    void appendSignInLog() {
        // given
        SignInLogEntity signInLogEntity = spy(new SignInLogEntity("127.0.0.1", "memberKey"));
        given(signInLogEntity.getId()).willReturn(1L);
        given(signInLogRepository.save(BDDMockito.any())).willReturn(signInLogEntity);

        // when
        Long id = signInLogAppender.append("127.0.0.1", "memberKey");

        // then
        Assertions.assertThat(id).isEqualTo(1L);
    }
}