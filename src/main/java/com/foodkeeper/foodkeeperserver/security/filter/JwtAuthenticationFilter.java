package com.foodkeeper.foodkeeperserver.security.filter;

import com.foodkeeper.foodkeeperserver.auth.domain.AuthMember;
import com.foodkeeper.foodkeeperserver.auth.implement.MemberRoleFinder;
import com.foodkeeper.foodkeeperserver.member.domain.Member;
import com.foodkeeper.foodkeeperserver.member.implement.MemberFinder;
import com.foodkeeper.foodkeeperserver.auth.implement.JwtValidator;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NullMarked;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String BEARER = "Bearer ";

    private final MemberFinder memberFinder;
    private final MemberRoleFinder memberRoleFinder;
    private final JwtValidator jwtValidator;

    @Override
    @NullMarked
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        getTokenFromHeader(request).ifPresent(this::authenticate);

        doFilter(request, response, filterChain);
    }

    private void authenticate(String token) {
        log.info("[Authentication Token]: {}", token);
        if (isBearerToken(token)) {
            String subject = jwtValidator.getSubjectIfValid(token.substring(BEARER.length()));
            Member member = memberFinder.find(subject);
            List<SimpleGrantedAuthority> authorities = memberRoleFinder.findAll(subject).getAuthorities();
            AuthMember authMember = new AuthMember(member, new HashMap<>(), authorities);

            SecurityContextHolder.getContext()
                    .setAuthentication(new UsernamePasswordAuthenticationToken(authMember, null, authorities));
        }
    }

    private Optional<String> getTokenFromHeader(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION));
    }

    private boolean isBearerToken(String token) {
        return token.startsWith(BEARER);
    }
}
