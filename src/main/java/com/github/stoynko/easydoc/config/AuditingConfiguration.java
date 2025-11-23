package com.github.stoynko.easydoc.config;

import com.github.stoynko.easydoc.security.SecurityAuditorAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditor", dateTimeProviderRef = "timeProvider")
public class AuditingConfiguration {

    @Bean
    AuditorAware<UUID> auditor() {
        return new SecurityAuditorAware();
    }

    @Bean
    DateTimeProvider timeProvider() {
        return () -> Optional.of(Instant.now());
    }


}