package com.github.stoynko.easydoc.web.handler;

import com.github.stoynko.easydoc.user.model.User;
import com.github.stoynko.easydoc.security.UserAuthenticationDetails;
import com.github.stoynko.easydoc.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;

import static com.github.stoynko.easydoc.user.model.AccountStatus.INCOMPLETE;

@Slf4j
@Component
@RequiredArgsConstructor
public class SuccessHandler implements AuthenticationSuccessHandler {

    private final RedirectStrategy redirect = new DefaultRedirectStrategy();
    private final RequestCache requestCache = new HttpSessionRequestCache();
    private final UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse,
                                        Authentication authentication)
            throws IOException {

        UserAuthenticationDetails principal = (UserAuthenticationDetails) authentication.getPrincipal();
        log.info("[login-success] | User with id {} successfully logged at :{}", principal.getId(), Instant.now());

        User user = userService.getUserById(principal.getId());

        if (user.getAccountStatus() == INCOMPLETE) {
            redirect.sendRedirect(httpServletRequest, httpServletResponse, "/onboarding/account");
            return;
        }

        SavedRequest request = requestCache.getRequest(httpServletRequest, httpServletResponse);

        if (request != null) {
            String targetedUrl = request.getRedirectUrl();
            requestCache.removeRequest(httpServletRequest, httpServletResponse);
            redirect.sendRedirect(httpServletRequest, httpServletResponse, targetedUrl);
            return;
        }

        redirect.sendRedirect(httpServletRequest, httpServletResponse, "/dashboard");
    }
}
