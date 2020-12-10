package com.syn.domo.web.controllers;

import com.syn.domo.model.binding.BuildingConstructModel;
import com.syn.domo.model.view.BuildingViewModel;
import com.syn.domo.service.FloorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/building")
public class BuildingController {
    private static final String CONSTRUCT_BUILDING = "Construct building";
    private static final String BUILDING_DETAILS = "Building Details";

    private final FloorService floorService;

    @Autowired
    public BuildingController(FloorService floorService) {
        this.floorService = floorService;
    }

    @GetMapping("/manage")
    public ModelAndView add(RedirectAttributes redirectAttributes, ModelAndView modelAndView) {
        if (this.floorService.isBuilt()) {
            redirectAttributes.addFlashAttribute("isBuilt", true);
            redirectAttributes.addFlashAttribute("building", this.floorService.getBuildingDetails());
            modelAndView.setViewName("redirect:/building/details");
        } else {
            modelAndView.addObject("pageTitle", CONSTRUCT_BUILDING);
            modelAndView.setViewName("building");
        }

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
            if (!this.floorService.isBuilt()) {
                BuildingViewModel building = this.floorService.constructBuilding(buildingConstructModel);
                redirectAttributes.addFlashAttribute("building", building);
                redirectAttributes.addFlashAttribute("isBuilt", true);
            }
            modelAndView.setViewName("redirect:/building/details");
        }
        return modelAndView;
    }

    @GetMapping("/details")
    public ModelAndView details(@ModelAttribute("building")
                                                BuildingViewModel building,
                                    ModelAndView modelAndView) {
        modelAndView.addObject("pageTitle", BUILDING_DETAILS);
        modelAndView.setViewName("building");
        return modelAndView;
    }
}
