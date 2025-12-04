package com.github.stoynko.easydoc.config;

import com.github.stoynko.easydoc.security.UserAuthenticationDetails;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

@Configuration
public class PrescriptionFeignConfig {

    @Bean
    public RequestInterceptor requestInterceptor() {
        return requestTemplate -> {
            SecurityContext context = SecurityContextHolder.getContext();
            if (context == null || context.getAuthentication() == null || !context.getAuthentication().isAuthenticated()) {
                return;
            }

            UserAuthenticationDetails principal = (UserAuthenticationDetails) context.getAuthentication().getPrincipal();
            requestTemplate.header("user-id", principal.getId().toString());
        };
    }
}
