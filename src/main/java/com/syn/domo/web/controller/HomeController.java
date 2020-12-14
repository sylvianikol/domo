package com.syn.domo.web.controller;

import com.syn.domo.model.view.BuildingViewModel;
import com.syn.domo.service.BuildingService;
import com.syn.domo.service.FloorService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    private static final String HOME_TITLE = "Administration Area";
    private final BuildingService buildingService;
    private final FloorService floorService;
    private final ModelMapper modelMapper;

    @Autowired
    public HomeController(BuildingService buildingService, FloorService floorService, ModelMapper modelMapper) {
        this.buildingService = buildingService;
        this.floorService = floorService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/")
    public ModelAndView home(ModelAndView modelAndView) {
        if (this.buildingService.hasBuildings()) {
            Set<BuildingViewModel> buildings = this.buildingService.getAllBuildings().stream()
                    .map(buildingServiceModel -> this.modelMapper.map(buildingServiceModel, BuildingViewModel.class))
                    .collect(Collectors.toSet());

            modelAndView.addObject("hasBuildings", true);
            modelAndView.addObject("buildings", buildings);
        }

        modelAndView.addObject("pageTitle", HOME_TITLE);
        modelAndView.setViewName("admin-home");
        return modelAndView;
    }
}
