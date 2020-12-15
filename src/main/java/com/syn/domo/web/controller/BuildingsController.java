package com.syn.domo.web.controller;

import com.syn.domo.model.binding.BuildingAddBindingModel;
import com.syn.domo.model.entity.Building;
import com.syn.domo.model.view.BuildingViewModel;
import com.syn.domo.service.BuildingService;
import com.syn.domo.web.controller.namespace.BuildingsNamespace;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class BuildingsController implements BuildingsNamespace {
    private static final String MANAGE_BUILDINGS = "Manage Buildings";
    private static final String ADD_BUILDING = "Add Building";
    private static final String BUILDINGS_DETAILS = "Buildings Details";

    private final BuildingService buildingService;
    private final ModelMapper modelMapper;

    @Autowired
    public BuildingsController(BuildingService buildingService, ModelMapper modelMapper) {
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
            modelAndView.addObject("pageH3Title", BUILDINGS_DETAILS);
        }

        modelAndView.addObject("buildings", buildings);
        modelAndView.addObject("pageTitle", MANAGE_BUILDINGS);
        modelAndView.addObject("pageH2Title", ADD_BUILDING);
        modelAndView.setViewName("manage-buildings");
        return modelAndView;
    }

    @PostMapping("/add")
    public ModelAndView add(@Valid @ModelAttribute("buildingConstructModel")
                                            BuildingAddBindingModel buildingAddBindingModel,
                            BindingResult bindingResult, ModelAndView modelAndView,
                            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Something went wrong");
        } else {

            BuildingViewModel buildingDetails = this.modelMapper.map(
                    this.buildingService.addBuilding(buildingAddBindingModel),
                    BuildingViewModel.class);
            redirectAttributes.addFlashAttribute("addedBuilding", buildingDetails.toString());
        }

        modelAndView.setViewName("redirect:/building/manage");
        return modelAndView;
    }

    @GetMapping("/manage/{id}")
    public ModelAndView manageBuilding(@PathVariable(value = "id") String id, ModelAndView modelAndView) {

        return modelAndView;
    }
}
