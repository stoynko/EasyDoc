package com.github.stoynko.easydoc.web.controllers;

import com.github.stoynko.easydoc.security.UserAuthenticationDetails;
import com.github.stoynko.easydoc.web.dto.DtoContext;
import com.github.stoynko.easydoc.web.utilities.PageBuilder;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import static com.github.stoynko.easydoc.web.model.ViewPage.ERROR;

@Controller
public class GlobalErrorController implements ErrorController{

    private final PageBuilder pageBuilder;

    public GlobalErrorController(PageBuilder pageBuilder) {
        this.pageBuilder = pageBuilder;
    }

    @RequestMapping("/error")
    public ModelAndView handleError(@AuthenticationPrincipal UserAuthenticationDetails principal) {

        ModelAndView modelAndView = pageBuilder.buildPage(DtoContext.forPage(ERROR, principal));
        modelAndView.addObject("errorMessage", "page_render_failed");

        return modelAndView;
    }
}
