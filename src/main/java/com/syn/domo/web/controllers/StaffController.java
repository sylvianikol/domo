package com.syn.domo.web.controllers;

import com.syn.domo.model.entity.JobRole;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/staff")
public class StaffController {

    private static final String ADD_STAFF_PAGE_TITLE = "Add Staff Member";

    @GetMapping("/add")
    public ModelAndView add(ModelAndView modelAndView) {

        modelAndView.addObject("jobRoles", JobRole.values());
        modelAndView.addObject("pageTitle", ADD_STAFF_PAGE_TITLE);
        modelAndView.setViewName("add-staff");
        return modelAndView;
    }
}
