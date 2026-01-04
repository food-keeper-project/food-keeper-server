package com.foodkeeper.foodkeeperserver.security.filter;

import com.foodkeeper.foodkeeperserver.security.exception.AuthenticationExceptionHandler;
import com.foodkeeper.foodkeeperserver.support.exception.AppException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NullMarked;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuthExceptionTranslationFilter extends OncePerRequestFilter {

    private final AuthenticationExceptionHandler exceptionHandler;

    @Override
    @NullMarked
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            doFilter(request, response, filterChain);
        } catch (AppException e) {
            exceptionHandler.handle(request, response, e);
        }
    }
}
