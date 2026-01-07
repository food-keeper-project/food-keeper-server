package com.foodkeeper.foodkeeperserver.security.filter;

import com.foodkeeper.foodkeeperserver.auth.dataaccess.entity.MemberRoleEntity;
import com.foodkeeper.foodkeeperserver.auth.dataaccess.repository.MemberRoleRepository;
import com.foodkeeper.foodkeeperserver.auth.dataaccess.repository.OauthRepository;
import com.foodkeeper.foodkeeperserver.auth.domain.Jwt;
import com.foodkeeper.foodkeeperserver.auth.domain.enums.MemberRole;
import com.foodkeeper.foodkeeperserver.auth.implement.JwtGenerator;
import com.foodkeeper.foodkeeperserver.auth.implement.JwtValidator;
import com.foodkeeper.foodkeeperserver.auth.implement.MemberRoleFinder;
import com.foodkeeper.foodkeeperserver.member.dataaccess.entity.MemberEntity;
import com.foodkeeper.foodkeeperserver.member.dataaccess.repository.MemberRepository;
import com.foodkeeper.foodkeeperserver.member.fixture.MemberEntityFixture;
import com.foodkeeper.foodkeeperserver.member.implement.MemberFinder;
import com.foodkeeper.foodkeeperserver.support.exception.AppException;
import com.foodkeeper.foodkeeperserver.support.exception.ErrorType;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock MemberRepository memberRepository;
    @Mock OauthRepository oauthRepository;
    @Mock MemberRoleRepository memberRoleRepository;
    JwtAuthenticationFilter jwtAuthenticationFilter;
    SecretKey secretKey = Keys.hmacShaKeyFor("this_is_a_test_secret_key_abcdefghijtlmnopqr"
            .getBytes(StandardCharsets.UTF_8));
    String memberKey = "memberKey";
    Jwt jwt;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
        MemberFinder memberFinder = new MemberFinder(memberRepository);
        MemberRoleFinder memberRoleFinder = new MemberRoleFinder(memberRoleRepository);
        jwt = new JwtGenerator(secretKey).generateJwt(memberKey);
        JwtValidator jwtValidator = new JwtValidator(secretKey);
        jwtAuthenticationFilter = new JwtAuthenticationFilter(memberFinder, memberRoleFinder, jwtValidator);
    }

    @Test
    @DisplayName("헤더에 유효한 Bearer Token이 있으면 인증에 성공한다.")
    void authenticatedIfExistsTokenInHeader() throws Exception {
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        MockHttpServletResponse httpServletResponse = new MockHttpServletResponse();
        FilterChain mockFilterChain = mock(FilterChain.class);
        MemberEntity memberEntity = MemberEntityFixture.DEFAULT.get();
        MemberRoleEntity memberRoleEntity = new MemberRoleEntity(MemberRole.ROLE_USER, memberKey);
        httpServletRequest.addHeader(HttpHeaders.AUTHORIZATION, "Bearer " + jwt.accessToken());
        given(memberRepository.findByMemberKey(eq(memberKey))).willReturn(Optional.of(memberEntity));
        given(memberRoleRepository.findByMemberKey(eq(memberKey))).willReturn(List.of(memberRoleEntity));

        jwtAuthenticationFilter.doFilterInternal(
                httpServletRequest, httpServletResponse, mockFilterChain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNotNull();
        assertThat(SecurityContextHolder.getContext().getAuthentication().isAuthenticated())
                .isTrue();
    }

    @Test
    @DisplayName("헤더에 Token이 Bearer Type이 아니면 인증에 실패한다.")
    void notAuthenticatedIfTokenTypeIsNotBearer() throws ServletException, IOException {
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        MockHttpServletResponse httpServletResponse = new MockHttpServletResponse();
        FilterChain mockFilterChain = mock(FilterChain.class);
        httpServletRequest.addHeader(HttpHeaders.AUTHORIZATION, "Basic " + jwt.accessToken());

        jwtAuthenticationFilter.doFilterInternal(
                httpServletRequest, httpServletResponse, mockFilterChain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    @DisplayName("헤더에 Token이 없으면 인증에 실패한다.")
    void notAuthenticatedIfNotExistsTokenInHeader() throws ServletException, IOException {
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        MockHttpServletResponse httpServletResponse = new MockHttpServletResponse();
        FilterChain mockFilterChain = mock(FilterChain.class);

        jwtAuthenticationFilter.doFilterInternal(
                httpServletRequest, httpServletResponse, mockFilterChain);

        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();
    }

    @Test
    @DisplayName("헤더에 Token이 유효하지 않으면 인증에 실패한다.")
    void notAuthenticatedIfTokenIsInvalid() {
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest();
        MockHttpServletResponse httpServletResponse = new MockHttpServletResponse();
        FilterChain mockFilterChain = mock(FilterChain.class);
        String token = "Bearer token";
        httpServletRequest.addHeader(HttpHeaders.AUTHORIZATION, token);

        assertThatThrownBy(
                () ->
                        jwtAuthenticationFilter.doFilterInternal(
                                httpServletRequest, httpServletResponse, mockFilterChain))
                .isInstanceOf(AppException.class)
                .extracting("errorType")
                .isEqualTo(ErrorType.MALFORMED_JWT);
    }
}