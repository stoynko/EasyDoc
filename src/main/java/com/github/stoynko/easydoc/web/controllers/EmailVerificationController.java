package com.github.stoynko.easydoc.web.controllers;

import com.github.stoynko.easydoc.security.UserAuthenticationDetails;
import com.github.stoynko.easydoc.services.UserService;
import com.github.stoynko.easydoc.web.dto.DtoContext;
import com.github.stoynko.easydoc.web.utilities.PageBuilder;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import static com.github.stoynko.easydoc.web.model.ViewPage.CONFIRMATION;

@RestController
public class EmailVerificationController {

    private final UserService userService;
    private final PageBuilder pageBuilder;

    @Autowired
    public EmailVerificationController(UserService userService, PageBuilder pageBuilder) {
        this.userService = userService;
        this.pageBuilder = pageBuilder;
    }

    @GetMapping("/auth/verify-email")
    public ModelAndView verifyEmail(@RequestParam("token") UUID tokenId,
                                    @AuthenticationPrincipal UserAuthenticationDetails principal) {

            userService.verifyEmailAddress(tokenId);
            ModelAndView modelAndView = pageBuilder.buildPage(DtoContext.forPage(CONFIRMATION, principal));
            return modelAndView.addObject("confirmationMessage", "emailVerificationSuccess");
    }
}
