package com.syn.domo.web.controller;

import com.syn.domo.model.binding.BuildingAddBindingModel;
import com.syn.domo.model.service.BuildingServiceModel;
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
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class BuildingsController implements BuildingsNamespace {
    private static final String MANAGE_BUILDINGS = "Manage Buildings";
    private static final String ADD_BUILDING = "Add Building";
    private static final String ALL_BUILDINGS_DETAILS = "All Buildings";
    private static final String BUILDING_DETAILS = "Building Details";

    private final BuildingService buildingService;
    private final ModelMapper modelMapper;

    @Autowired
    public BuildingsController(BuildingService buildingService, ModelMapper modelMapper) {
        this.buildingService = buildingService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/")
    public ModelAndView manage(ModelAndView modelAndView) {

        if (this.buildingService.hasBuildings()) {
            modelAndView.addObject("hasBuildings", true);
            modelAndView.addObject("pageH3Title", ALL_BUILDINGS_DETAILS);
            Set<BuildingViewModel> buildings = this.buildingService.getAllBuildings().stream()
                    .map(buildingServiceModel -> this.modelMapper.map(buildingServiceModel, BuildingViewModel.class))
                    .collect(Collectors.toCollection(LinkedHashSet::new));
            modelAndView.addObject("buildings", buildings);
        }

        modelAndView.addObject("pageTitle", MANAGE_BUILDINGS);
        modelAndView.addObject("pageH2Title", ADD_BUILDING);
        modelAndView.setViewName("manage-buildings");
        return modelAndView;
    }

    @PostMapping("/")
    public ModelAndView add(@Valid @ModelAttribute("buildingConstructModel")
                                            BuildingAddBindingModel buildingAddBindingModel,
                            BindingResult bindingResult, ModelAndView modelAndView,
                            RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("error", "Something went wrong");
        } else {
            BuildingViewModel buildingDetails = this.modelMapper.map(
                    this.buildingService.addBuilding(
                            this.modelMapper.map(buildingAddBindingModel, BuildingServiceModel.class)),
                    BuildingViewModel.class);

            redirectAttributes.addFlashAttribute("buildingDetails", buildingDetails.toString());
        }

        modelAndView.setViewName("redirect:/buildings/");
        return modelAndView;
    }

    @GetMapping("/{buildingId}")
    public ModelAndView manageBuilding(@PathVariable(value = "buildingId") String buildingId, ModelAndView modelAndView) {
        BuildingViewModel building = this.modelMapper.map(
                this.buildingService.getById(buildingId), BuildingViewModel.class);

        if (building.getApartments().size() > 0) {
            modelAndView.addObject("hasApartments", true);
        }

        modelAndView.addObject("building", building);
        modelAndView.addObject("pageTitle", BUILDING_DETAILS);
        modelAndView.setViewName("details-building.html");
        return modelAndView;
    }
}
