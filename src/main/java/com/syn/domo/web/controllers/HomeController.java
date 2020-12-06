package com.syn.domo.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

    private static final String HOME_TITLE = "Administration Area";

    @GetMapping("/")
    public ModelAndView home(ModelAndView modelAndView) {
        modelAndView.addObject("pageTitle", HOME_TITLE);
        modelAndView.setViewName("admin-home");
        return modelAndView;
    }
}
