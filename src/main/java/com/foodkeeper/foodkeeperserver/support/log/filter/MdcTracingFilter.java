package com.foodkeeper.foodkeeperserver.support.log.filter;

import jakarta.servlet.*;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Order(1)
@Component
public class MdcTracingFilter implements Filter {

    private static final String TRACE_ID = "traceId";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        MDC.put(TRACE_ID, UUID.randomUUID().toString().substring(0, 8));

        try {
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(TRACE_ID);
        }
    }
}
