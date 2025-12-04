package com.github.stoynko.easydoc.web.advice;

import com.github.stoynko.easydoc.security.UserAuthenticationDetails;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class NavigationModelAdvice {

    @ModelAttribute("principal")
    public UserAuthenticationDetails principal(@AuthenticationPrincipal UserAuthenticationDetails principal) {
        return principal;
    }

    @ModelAttribute("currentPath")
    public String getCurrentPath(HttpServletRequest servletRequest) {
        String uri = servletRequest.getRequestURI();
        return (uri != null) ? uri : "";
    }
}
