package com.foodkeeper.foodkeeperserver.support.ratelimit.filter;

import com.foodkeeper.foodkeeperserver.common.utils.NetworkUtils;
import com.foodkeeper.foodkeeperserver.support.exception.ErrorType;
import com.foodkeeper.foodkeeperserver.support.ratelimit.LimitApi;
import com.foodkeeper.foodkeeperserver.support.response.ApiResponse;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.BucketConfiguration;
import io.github.bucket4j.ConsumptionProbe;
import io.github.bucket4j.distributed.proxy.ProxyManager;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@Order(2)
@Component
@RequiredArgsConstructor
public class ApiThrottlingFilter implements Filter {
    private static final List<LimitApi> LIMIT_APIS =
            List.of(
                    LimitApi.pattern("/api/v1/auth/sign-in/**"),
                    LimitApi.pattern("POST", "/api/v1/auth/check/account"),
                    LimitApi.pattern("POST", "/api/v1/auth/verify/email")
            );
    public static final int ONE_SECOND_TO_NANOS = 1_000_000_000;

    private final ProxyManager<String> proxyManager;
    private final BucketConfiguration bucketConfiguration;
    private final AntPathMatcher antPathMatcher;
    private final ObjectMapper objectMapper;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;

        for (LimitApi limitApi : LIMIT_APIS) {
            if (antPathMatcher.match(limitApi.url(), httpRequest.getRequestURI())
                    && (limitApi.noMethod() || limitApi.method().equals(httpRequest.getMethod()))) {
                String clientIp = NetworkUtils.getClientIp(httpRequest);
                Bucket bucket = proxyManager.getProxy(clientIp, () -> bucketConfiguration);

                checkApiToken(bucket, clientIp, chain, request, response);
                return;
            }
        }

        chain.doFilter(request, response);
    }

    private void checkApiToken(
            Bucket bucket,
            String clientIp,
            FilterChain chain,
            ServletRequest request,
            ServletResponse response)
            throws IOException, ServletException {
        ConsumptionProbe probe = bucket.tryConsumeAndReturnRemaining(1);

        if (probe.isConsumed()) {
            chain.doFilter(request, response);
            return;
        }

        long waitForRefill = probe.getNanosToWaitForRefill() / ONE_SECOND_TO_NANOS;

        log.warn("[REQUEST BLOCKED - 429]: IP={} | Path={}", clientIp, ((HttpServletRequest) request).getRequestURI());
        responseTooMuchRequests(response, waitForRefill);
    }

    private void responseTooMuchRequests(ServletResponse response, long waitForRefill) throws IOException {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
        httpResponse.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
        httpResponse.setCharacterEncoding(StandardCharsets.UTF_8.name());

        String responseValue = objectMapper.writeValueAsString(
                ApiResponse.error(ErrorType.TOO_MANY_REQUESTS, waitForRefill + "초 뒤에 다시 시도해주세요"));
        response.getWriter().write(responseValue);
    }

}
