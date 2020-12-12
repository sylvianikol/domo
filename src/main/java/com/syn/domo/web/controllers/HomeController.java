package com.syn.domo.web.controllers;

import com.syn.domo.model.view.BuildingViewModel;
import com.syn.domo.service.BuildingService;
import com.syn.domo.service.FloorService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

    private static final String HOME_TITLE = "Administration Area";
    private final BuildingService buildingService;
    private final FloorService floorService;

    public HomeController(BuildingService buildingService, FloorService floorService) {
        this.buildingService = buildingService;
        this.floorService = floorService;
    }

    @GetMapping("/")
    public ModelAndView home(ModelAndView modelAndView) {
        if (this.buildingService.isBuilt()) {
            modelAndView.addObject("isBuilt", true);
            BuildingViewModel building = this.buildingService.getBuildingDetails(1L);
            modelAndView.addObject("building", building);
        } else {
            modelAndView.addObject("isBuilt", false);
        }
        modelAndView.addObject("pageTitle", HOME_TITLE);
        modelAndView.setViewName("admin-home");
        return modelAndView;
    }
}
