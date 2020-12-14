package com.syn.domo.web.controller;

import com.syn.domo.model.binding.BuildingConstructModel;
import com.syn.domo.model.view.BuildingViewModel;
import com.syn.domo.service.BuildingService;
import com.syn.domo.web.controller.namespace.BuildingNamespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class BuildingController implements BuildingNamespace {
    private static final String CONSTRUCT_BUILDING = "Construct building";
    private static final String BUILDING_DETAILS = "Building Details";

    private final BuildingService buildingService;

    @Autowired
    public BuildingController(BuildingService buildingService) {
        this.buildingService = buildingService;
    }

    @GetMapping("/manage")
    public ModelAndView add(RedirectAttributes redirectAttributes, ModelAndView modelAndView) {
        if (this.buildingService.isBuilt()) {
            modelAndView.addObject("isBuilt", true);
            modelAndView.addObject("pageTitle", BUILDING_DETAILS);
            // TODO: change to getBuildingDetails(loggedAdminId)
            modelAndView.addObject("building", this.buildingService.getBuildingDetails(1L));
        } else {
            modelAndView.addObject("pageTitle", CONSTRUCT_BUILDING);
        }

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
            if (!this.buildingService.isBuilt()) {
                BuildingViewModel building = this.buildingService.constructBuilding(buildingConstructModel);
                redirectAttributes.addFlashAttribute("building", building);
                redirectAttributes.addFlashAttribute("isBuilt", true);
            }
        }

        modelAndView.setViewName("redirect:/building/manage");
        return modelAndView;
    }
}
