package com.syn.domo.web.controller;

import com.syn.domo.model.binding.BuildingConstructModel;
import com.syn.domo.model.view.BuildingViewModel;
import com.syn.domo.service.BuildingService;
import com.syn.domo.web.controller.namespace.BuildingNamespace;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class BuildingController implements BuildingNamespace {
    private static final String MANAGE_BUILDINGS = "Manage buildings";
    private static final String BUILDING_DETAILS = "Building Details";

    private final BuildingService buildingService;
    private final ModelMapper modelMapper;

    @Autowired
    public BuildingController(BuildingService buildingService, ModelMapper modelMapper) {
        this.buildingService = buildingService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/manage")
    public ModelAndView manage(ModelAndView modelAndView) {
        Set<BuildingViewModel> buildings = this.buildingService.getAllBuildings().stream()
                .map(buildingServiceModel -> this.modelMapper.map(buildingServiceModel, BuildingViewModel.class))
                .collect(Collectors.toSet());

        if (this.buildingService.hasBuildings()) {
            modelAndView.addObject("hasBuildings", true);
        }

        modelAndView.addObject("buildings", buildings);

        modelAndView.addObject("pageTitle", MANAGE_BUILDINGS);
        modelAndView.setViewName("manage-building");
        return modelAndView;
    }

    @PostMapping("/add")
    public ModelAndView addPost(@Valid @ModelAttribute("buildingConstructModel")
                                            BuildingConstructModel buildingConstructModel,
                          BindingResult bindingResult, ModelAndView modelAndView,
                                RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("redirect:/building/manage");
        } else {

            BuildingViewModel building =
                    this.modelMapper.map(this.buildingService.constructBuilding(buildingConstructModel), BuildingViewModel.class);
            redirectAttributes.addFlashAttribute("building", building);
        }

        modelAndView.setViewName("redirect:/building/manage");
        return modelAndView;
    }
}
