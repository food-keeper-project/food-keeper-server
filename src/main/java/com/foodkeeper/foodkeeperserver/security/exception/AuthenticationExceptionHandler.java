package com.foodkeeper.foodkeeperserver.security.exception;

import com.foodkeeper.foodkeeperserver.support.exception.AppException;
import com.foodkeeper.foodkeeperserver.support.exception.ErrorType;
import com.foodkeeper.foodkeeperserver.support.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
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

    private static final String[] HEADERS = {
            "X-Forwarded-For",
            "X-Real-IP",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_CLIENT_IP",
            "HTTP_X_FORWARDED_FOR"
    };
    private static final String UNKNOWN = "unknown";

    private final ObjectMapper objectMapper;

    public void handle(HttpServletRequest request, HttpServletResponse response, AppException exception)
            throws IOException {
        if (response.isCommitted()) {
            return;
        }
        writeUnauthorizedResponse(request, response, ApiResponse.error(exception.getErrorType()));
    }

    public void handle(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
            throws IOException {
        if (response.isCommitted()) {
            return;
        }
        writeUnauthorizedResponse(request, response, ApiResponse.error(resolveErrorCode(exception)));
    }

    private void writeUnauthorizedResponse(HttpServletRequest request, HttpServletResponse response,
                                           ApiResponse<Void> body)
            throws IOException {
        String clientIp = getClientIp(request);
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String userAgent = request.getHeader("User-Agent");
        String errorCode = body.error().errorCode();
        String message = body.error().message();
        log.warn("[Authentication Exception]: IP={} | Method={} | URI={} | UserAgent={} | errorCode={} | message={}",
                clientIp, method, uri, userAgent, errorCode, message);

        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());

        PrintWriter writer = response.getWriter();
        writer.write(objectMapper.writeValueAsString(ResponseEntity.status(HttpStatus.UNAUTHORIZED.value()).body(body)));
        writer.flush();
    }

    private ErrorType resolveErrorCode(AuthenticationException e) {
        if (e instanceof AuthenticationCredentialsNotFoundException
                || e instanceof InsufficientAuthenticationException) {
            return ErrorType.REQUIRED_AUTH;
        } else {
            return ErrorType.FAILED_AUTH;
        }
    }

    private String getClientIp(HttpServletRequest request) {
        for (String header : HEADERS) {
            String ip = request.getHeader(header);
            if (Strings.isNotBlank(ip) && !ip.equalsIgnoreCase(UNKNOWN)) {
                return ip.split(",")[0];
            }
        }

        return request.getRemoteAddr();
    }
}
