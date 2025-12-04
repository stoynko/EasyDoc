package com.github.stoynko.easydoc.security;

import com.github.stoynko.easydoc.shared.event.UserContextRefreshEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SecurityContextUpdater {

    private final UserDetailsService userDetailsService;

    @EventListener
    public void handleUserContextRefresh(UserContextRefreshEvent event) {
        refreshContext(event.userId());
    }

    public void refreshContext(UUID userId) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();

        if (authentication == null || authentication instanceof AnonymousAuthenticationToken
                || !(authentication.getPrincipal() instanceof UserAuthenticationDetails)) {
            return;
        }

        UserAuthenticationDetails principal = (UserAuthenticationDetails)authentication.getPrincipal();
        if (!principal.getId().equals(userId)) {
            return;
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(principal.getUsername());
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userDetails,
                authentication != null ? authentication.getCredentials() : null,
                userDetails.getAuthorities()
        );

        authenticationToken.setDetails(authentication.getDetails());
        securityContext.setAuthentication(authenticationToken);
    }

}
