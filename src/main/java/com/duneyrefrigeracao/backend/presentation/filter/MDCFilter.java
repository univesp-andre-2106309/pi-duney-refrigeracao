package com.duneyrefrigeracao.backend.presentation.filter;
import jakarta.servlet.*;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

@Component
public class MDCFilter implements Filter {

    private static final String MDC_KEY = "requestId";
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String requestId = UUID.randomUUID().toString().replace("-","").toUpperCase();
        ThreadContext.put(MDC_KEY, requestId);
        try{
            filterChain.doFilter(servletRequest,servletResponse);
        } finally {
            ThreadContext.remove(MDC_KEY);
        }
    }
}
