package com.techsophy.tsf.form.config;

import com.techsophy.multitenancy.mongo.config.TenantContext;
import com.techsophy.tsf.form.utils.TokenUtils;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import static com.techsophy.tsf.form.constants.FormModelerConstants.*;

@ComponentScan({PACKAGE_NAME, MULTI_TENANCY_PACKAGE_NAME})
@Configuration
@RequiredArgsConstructor
public class CustomFilter implements Filter {
    private final TokenUtils tokenUtils;

    @SneakyThrows
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String tenant = tokenUtils.getIssuerFromToken(httpRequest.getHeader(AUTHORIZATION));
        if (StringUtils.isNotEmpty(tenant)) {
            TenantContext.setTenantId(tenant);
        }
        chain.doFilter(request, response);
    }
}