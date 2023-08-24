package com.sft.config.security;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UrlPathHelper;

import java.io.IOException;

@RequiredArgsConstructor
@Order(Ordered.LOWEST_PRECEDENCE)
@Component("loggingFilter")
public class TenantServletHackFilter implements Filter {

    private final TenantIdentifierResolver tenantIdentifierResolver;

    @Override
    public void init(FilterConfig config) throws ServletException {
        // initialize something
    }

    @Override
    public void doFilter(
            ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
 
        HttpServletRequest req = (HttpServletRequest) request;
        val uriHelper = new UrlPathHelper();
        val path = uriHelper.getPathWithinApplication(req);
        val customerId = subStringBetween(path, "/customer/", "/");
        System.out.println(customerId);
        if (customerId != null) {
            System.out.println("Customer id found: " + customerId);
            tenantIdentifierResolver.setCurrentCustomerId(customerId);
        } else {
            tenantIdentifierResolver.setCurrentCustomerId("BOOTSTRAP");

        }
        chain.doFilter(request, response);
    }
    public String subStringBetween(String string, String firstString, String secondString) {
        int startIndex = string.indexOf(firstString);
        if (startIndex != -1) {
            startIndex += firstString.length();
            int endIndex = string.indexOf(secondString, startIndex);
            if (endIndex != -1) {
                return string.substring(startIndex, endIndex);
            }
        }
        return null;
    }

    @Override
    public void destroy() {
        // cleanup code, if necessary
    }
}
