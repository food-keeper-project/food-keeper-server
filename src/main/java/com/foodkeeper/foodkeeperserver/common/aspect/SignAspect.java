package com.foodkeeper.foodkeeperserver.common.aspect;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j
@Aspect
@Component
public class SignAspect {

    private static final String[] HEADERS = {
            "X-Forwarded-For",
            "X-Real-IP",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_CLIENT_IP",
            "HTTP_X_FORWARDED_FOR"
    };
    private static final String UNKNOWN = "unknown";

    @Pointcut("@within(org.springframework.web.bind.annotation.RestController) || " +
            "@within(org.springframework.stereotype.Controller)")
    private void controllers() {
    }

    @Around("controllers()")
    public Object apiExchange(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes == null) {
            return joinPoint.proceed();
        }

        HttpServletRequest request = attributes.getRequest();
        String clientIp = getClientIp(request);
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String userAgent = request.getHeader("User-Agent");
        log.info("[REQUEST API]: IP={} | Method={} | URI={} | UserAgent={}", clientIp, method, uri, userAgent);

        Object result = joinPoint.proceed();

        HttpServletResponse response = attributes.getResponse();
        int status = resolveStatus(response, result);
        log.info("[RESPONSE API]: status={}", status);

        return result;
    }

    private int resolveStatus(HttpServletResponse resp, Object result) {
        if (result instanceof ResponseEntity<?> re) {
            return re.getStatusCode().value();
        }

        if (resp != null) {
            return resp.getStatus();
        }

        return 200;
    }

    @Around("@annotation(com.foodkeeper.foodkeeperserver.common.aspect.annotation.SignInLog)")
    public Object signInLog(ProceedingJoinPoint joinPoint) throws Throwable {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (attributes != null) {
            HttpServletRequest request = attributes.getRequest();
            String clientIp = getClientIp(request);
            String method = request.getMethod();
            String uri = request.getRequestURI();
            String userAgent = request.getHeader("User-Agent");
            log.info("[SIGN IN]: IP={} | Method={} | URI={} | UserAgent={}", clientIp, method, uri, userAgent);
        }

        return joinPoint.proceed();
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
