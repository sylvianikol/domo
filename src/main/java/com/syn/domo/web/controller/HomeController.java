package com.syn.domo.web.controller;

import com.syn.domo.service.BuildingService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

    private static final String HOME_TITLE = "Administration Area";
    private final BuildingService buildingService;
    private final ModelMapper modelMapper;

    @Autowired
    public HomeController(BuildingService buildingService, ModelMapper modelMapper) {
        this.buildingService = buildingService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/")
    public ModelAndView home(ModelAndView modelAndView) {

        int buildingsCount = this.buildingService.getCount();
        if (buildingsCount > 0) {
            modelAndView.addObject("hasBuildings", true);
        }

        modelAndView.addObject("buildingsCount", buildingsCount);
        modelAndView.addObject("pageTitle", HOME_TITLE);
        modelAndView.setViewName("admin-home");
        return modelAndView;
    }
}
