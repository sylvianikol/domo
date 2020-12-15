package com.syn.domo.web.controller;

import com.syn.domo.model.binding.ApartmentAddBindingModel;
import com.syn.domo.model.service.ApartmentServiceModel;
import com.syn.domo.service.ApartmentService;
import com.syn.domo.service.FloorService;
import com.syn.domo.web.controller.namespace.ApartmentsNamespace;
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
public class ApartmentsController implements ApartmentsNamespace {
    private static final String MANAGE_APARTMENTS_TITLE = "Manage Apartments";
    private static final String ADD_APARTMENTS_TITLE = "Add Apartments";
    private static final String EDIT_APARTMENTS_TITLE = "Edit Apartments";

    private final ApartmentService apartmentService;
    private final FloorService floorService;
    private final ModelMapper modelMapper;

    @Autowired
    public ApartmentsController(ApartmentService apartmentService, FloorService floorService, ModelMapper modelMapper) {
        this.apartmentService = apartmentService;
        this.floorService = floorService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/manage")
    public ModelAndView manage(@PathVariable(value = "buildingId") String buildingId, ModelAndView modelAndView) {
        // TODO: getAllApartmentByBuildingId(buildingId)
        Set<ApartmentServiceModel> apartments = this.apartmentService.getAllApartments();
        if (apartments.size() > 0) {
            modelAndView.addObject("hasApartments", true);
            modelAndView.addObject("pageH3Title", EDIT_APARTMENTS_TITLE);
            modelAndView.addObject("apartments", apartments);
        }

        modelAndView.addObject("pageH2Title", ADD_APARTMENTS_TITLE);
        modelAndView.addObject("floorNumbers", this.floorService.getAllFloorNumbers());
        modelAndView.addObject("pageTitle", MANAGE_APARTMENTS_TITLE);
        modelAndView.setViewName("manage-apartments");

        return modelAndView;
    }

    @PostMapping("/add")
    public ModelAndView addPost(@Valid @ModelAttribute("apartmentAddBindingModel")
                                            ApartmentAddBindingModel apartmentAddBindingModel,
                                BindingResult bindingResult, ModelAndView modelAndView,
                                RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("redirect:/apartments/manage");
        } else {
            String apartmentNumber = apartmentAddBindingModel.getNumber();
            Integer floorNumber = apartmentAddBindingModel.getFloorNumber();

            if (this.apartmentService.getByNumber(apartmentNumber) != null) {
                redirectAttributes.addFlashAttribute("alreadyExists",true);
            } else {
                this.apartmentService.add(this.modelMapper
                        .map(apartmentAddBindingModel, ApartmentServiceModel.class));
                redirectAttributes.addFlashAttribute("success",true);
            }

            redirectAttributes.addFlashAttribute("floorNumber", floorNumber);
            redirectAttributes.addFlashAttribute("apartmentNumber", apartmentNumber);
            modelAndView.setViewName("redirect:/apartments/manage");
        }

        return modelAndView;
    }
}
