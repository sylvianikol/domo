package com.syn.domo.web.controller;

import com.syn.domo.model.binding.ApartmentAddBindingModel;
import com.syn.domo.model.service.ApartmentServiceModel;
import com.syn.domo.service.ApartmentService;
import com.syn.domo.service.BuildingService;
import com.syn.domo.service.FloorService;
import com.syn.domo.web.controller.namespace.BuildingsNamespace;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Set;

@Controller
public class ApartmentsController implements BuildingsNamespace {
    private static final String MANAGE_APARTMENTS_TITLE = "Manage Apartments";
    private static final String ADD_APARTMENTS_TITLE = "Add Apartments";
    private static final String EDIT_APARTMENTS_TITLE = "Edit Apartments";

    private final ApartmentService apartmentService;
    private final BuildingService buildingService;
    private final FloorService floorService;
    private final ModelMapper modelMapper;

    @Autowired
    public ApartmentsController(ApartmentService apartmentService, BuildingService buildingService, FloorService floorService, ModelMapper modelMapper) {
        this.apartmentService = apartmentService;
        this.buildingService = buildingService;
        this.floorService = floorService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/{buildingId}/apartments/")
    public ModelAndView manage(@PathVariable(value = "buildingId") String buildingId,
                               ModelAndView modelAndView) {

        Set<ApartmentServiceModel> apartments =
                this.apartmentService.getAllApartmentsByBuildingId(buildingId);

        if (apartments.size() > 0) {
            modelAndView.addObject("hasApartments", true);
            modelAndView.addObject("pageH3Title", EDIT_APARTMENTS_TITLE);
            modelAndView.addObject("apartments", apartments);
        }

        modelAndView.addObject("pageH2Title", ADD_APARTMENTS_TITLE);
        modelAndView.addObject("floorNumbers", this.floorService.getAllFloorNumbers());
        modelAndView.addObject("buildingId", buildingId);
        modelAndView.addObject("pageTitle",
                this.buildingService.getBuildingName(buildingId)
                        + ": " + MANAGE_APARTMENTS_TITLE);
        modelAndView.setViewName("manage-apartments");

        return modelAndView;
    }

    @PostMapping("/{buildingId}/apartments/")
    public ModelAndView add(@PathVariable(value = "buildingId") String buildingId,
                            @Valid @ModelAttribute("apartmentAddBindingModel")
                                            ApartmentAddBindingModel apartmentAddBindingModel,
                            BindingResult bindingResult, ModelAndView modelAndView,
                            RedirectAttributes redirectAttributes) {
        System.out.println();
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("redirect:/buildings/" + buildingId + "/apartments/");
        } else {
            String apartmentNumber = apartmentAddBindingModel.getNumber();
            Integer floorNumber = apartmentAddBindingModel.getFloorNumber();

            if (this.apartmentService.alreadyExists(apartmentNumber, buildingId)) {
                redirectAttributes.addFlashAttribute("alreadyExists",true);
            } else {
                this.apartmentService.add(
                        this.modelMapper.map(apartmentAddBindingModel, ApartmentServiceModel.class),
                        buildingId);

                redirectAttributes.addFlashAttribute("success",true);
            }

            redirectAttributes.addFlashAttribute("floorNumber", floorNumber);
            redirectAttributes.addFlashAttribute("apartmentNumber", apartmentNumber);
            modelAndView.setViewName("redirect:/buildings/" + buildingId + "/apartments/");
        }

        return modelAndView;
    }
}
