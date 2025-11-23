package com.github.stoynko.easydoc.security;

import com.github.stoynko.easydoc.events.UserContextRefreshEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class SecurityContextUpdater {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

    @Autowired
    public SecurityContextUpdater(AuthenticationManager authenticationManager, UserDetailsService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
    }

    @EventListener
    public void handleUserContextRefresh(UserContextRefreshEvent event) {
        refreshContext(event.username());
    }

    public void refreshContext(String username) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);


        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                authentication != null ? authentication.getCredentials() : null,
                userDetails.getAuthorities()
        );

        if (authentication != null) {
            authenticationToken.setDetails(authentication.getDetails());
        }
        securityContext.setAuthentication(authenticationToken);
    }

}
