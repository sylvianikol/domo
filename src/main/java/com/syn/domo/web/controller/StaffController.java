package com.syn.domo.web.controller;

import com.syn.domo.service.JobService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/staff")
public class StaffController {

    private static final String ADD_STAFF_PAGE_TITLE = "Add Staff Member";
    private final JobService jobService;

    public StaffController(JobService jobService) {
        this.jobService = jobService;
    }


    @GetMapping("/add")
    public ModelAndView add(ModelAndView modelAndView) {

        modelAndView.addObject("jobRoles", this.jobService.getAllJobRoles());
        modelAndView.addObject("pageTitle", ADD_STAFF_PAGE_TITLE);
        modelAndView.setViewName("add-staff");
        return modelAndView;
    }
}
