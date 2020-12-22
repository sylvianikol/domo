package com.syn.domo.web.controller;

import com.syn.domo.service.BuildingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

    private static final String HOME_TITLE = "Administration Area";
    private final BuildingService buildingService;

    @Autowired
    public HomeController(BuildingService buildingService) {
        this.buildingService = buildingService;
    }

    @GetMapping("/")
    public ModelAndView home(ModelAndView modelAndView) {

        modelAndView.addObject("hasBuildings", this.buildingService.hasActiveBuildings())
            .addObject("buildingsCount", this.buildingService.getCount())
            .addObject("pageTitle", HOME_TITLE)
            .setViewName("admin-home");

        return modelAndView;
    }
}
