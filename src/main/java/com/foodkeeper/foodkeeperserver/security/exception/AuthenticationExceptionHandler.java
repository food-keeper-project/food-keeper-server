package com.foodkeeper.foodkeeperserver.security.exception;

import com.foodkeeper.foodkeeperserver.support.exception.AppException;
import com.foodkeeper.foodkeeperserver.support.exception.ErrorType;
import com.foodkeeper.foodkeeperserver.support.response.ApiResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationExceptionHandler {

    private final ObjectMapper objectMapper;

    public void handle(HttpServletResponse response, AppException exception)
            throws IOException {
        if (response.isCommitted()) {
            return;
        }
        writeUnauthorizedResponse(response, ApiResponse.error(exception.getErrorType()));
    }

    public void handle(HttpServletResponse response, AuthenticationException exception)
            throws IOException {
        if (response.isCommitted()) {
            return;
        }
        writeUnauthorizedResponse(response, ApiResponse.error(resolveErrorCode(exception)));
    }

    private void writeUnauthorizedResponse(HttpServletResponse response, ApiResponse<Void> body)
            throws IOException {
        log.info("Authentication Exception: {} {}", body.error().errorCode(), body.error().message());

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        PrintWriter writer = response.getWriter();
        writer.write(objectMapper.writeValueAsString(ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).body(body)));
        writer.flush();
    }

    private ErrorType resolveErrorCode(AuthenticationException e) {
        log.debug("Implementation of AuthenticationException is {}", e.getClass().getSimpleName());
        if (e instanceof AuthenticationCredentialsNotFoundException
                || e instanceof InsufficientAuthenticationException) {
            return ErrorType.REQUIRED_AUTH;
        } else {
            return ErrorType.FAILED_AUTH;
        }
    }
}
