package com.github.stoynko.easydoc.config;

import com.github.stoynko.easydoc.shared.exception.ErrorMessages;
import com.github.stoynko.easydoc.web.handler.SuccessHandler;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableMethodSecurity
public class WebConfiguration implements WebMvcConfigurer {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, SuccessHandler successHandler) throws Exception {

        http.sessionManagement(sessionManagement -> sessionManagement
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .securityContext(securityContext -> securityContext.requireExplicitSave(false))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .requestMatchers("/", "/auth/verify-email", "/doctors", "/onboarding/doctors","/register", "/error").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .usernameParameter("emailAddress")
                        .passwordParameter("password")
                        .successHandler(successHandler)
                        .failureHandler((request, response, exception) -> {
                            request.getSession().setAttribute("LOGIN_ERROR", ErrorMessages.CREDENTIALS_INVALID.getErrorMessage());
                            response.sendRedirect("/login");
                        })
                        .permitAll()
                ).logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
                        .invalidateHttpSession(true)
                        .deleteCookies("SESSION", "JSESSIONID")
                        .logoutSuccessUrl("/"));
        return http.build();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
