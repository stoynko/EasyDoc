package com.github.stoynko.easydoc.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ErrorController {

    @GetMapping("/401")
    public ModelAndView get401() {
        ModelAndView modelAndView = new ModelAndView("errors/401");
        return modelAndView;
    }

    @GetMapping("/404")
    public ModelAndView get404() {
        ModelAndView modelAndView = new ModelAndView("errors");
        return modelAndView;
    }

    @GetMapping("/test")
    public ModelAndView getTest() {
        return new ModelAndView("pages/medical-report-doctor");
    }

    @GetMapping("/test-2")
    public ModelAndView getTesttwo() {
        return new ModelAndView("pages/users");
    }

    @GetMapping("/test1")
    public ModelAndView getReportPage() {
        ModelAndView modelAndView = new ModelAndView("prescription_medicaments");
        return modelAndView;
    }

    @GetMapping("/test-p2")
    public ModelAndView getReportPage2() {
        ModelAndView modelAndView = new ModelAndView("prescription_create");
        return modelAndView;
    }
}
