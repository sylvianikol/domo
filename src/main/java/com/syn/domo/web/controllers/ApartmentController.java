package com.syn.domo.web.controllers;

import com.syn.domo.model.binding.ApartmentAddBindingModel;
import com.syn.domo.model.service.ApartmentServiceModel;
import com.syn.domo.model.view.ApartmentViewModel;
import com.syn.domo.service.ApartmentService;
import com.syn.domo.service.FloorService;
import com.syn.domo.web.controllers.namespace.ApartmentNamespace;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class ApartmentController implements ApartmentNamespace {
    private static final String MANAGE_APARTMENTS_TITLE = "Manage Apartments";
    private static final String ADD_APARTMENTS_TITLE = "Add Apartments";
    private static final String EDIT_APARTMENTS_TITLE = "Edit Apartments";
    private static final String APARTMENT_DETAILS = "Apartment Details";

    private final ApartmentService apartmentService;
    private final FloorService floorService;
    private final ModelMapper modelMapper;

    @Autowired
    public ApartmentController(ApartmentService apartmentService, FloorService floorService, ModelMapper modelMapper) {
        this.apartmentService = apartmentService;
        this.floorService = floorService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/manage")
    public ModelAndView manage(ModelAndView modelAndView) {
        if (this.floorService.isOverCapacity()) {
            modelAndView.addObject("buildingFull", true);
            modelAndView.addObject("pageSubTitle", EDIT_APARTMENTS_TITLE);
            modelAndView.addObject("apartments", this.apartmentService.getAllApartments());
        } else {
            modelAndView.addObject("buildingFull", false);
            modelAndView.addObject("pageSubTitle", ADD_APARTMENTS_TITLE);
        }
        modelAndView.addObject("floorNumbers", this.floorService.getAllFloorNumbers());
        modelAndView.addObject("pageTitle", MANAGE_APARTMENTS_TITLE);
        modelAndView.setViewName("manage-apartments");

        return modelAndView;
    }

    @PostMapping("/add")
    public ModelAndView add(@Valid @ModelAttribute("apartmentAddBindingModel")
                                            ApartmentAddBindingModel apartmentAddBindingModel,
                                BindingResult bindingResult, ModelAndView modelAndView,
                                RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("redirect:/apartments/manage");
        } else {
            String apartmentNumber = apartmentAddBindingModel.getNumber();
            Integer floorNumber = apartmentAddBindingModel.getFloorNumber();

            if (!this.floorService.hasCapacity(floorNumber)) {
                redirectAttributes.addFlashAttribute("noCapacity", true);
            } else if (this.apartmentService.getByNumber(apartmentNumber) != null) {
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

    @GetMapping("/details/{apartmentNumber}")
    public ModelAndView getAuthor(@PathVariable(value = "apartmentNumber") String number,
                                  ModelAndView modelAndView) {
       // TODO: ApartmentNotFoundException
        ApartmentViewModel apartment =
                this.modelMapper.map(this.apartmentService.getByNumber(number), ApartmentViewModel.class);
        modelAndView.addObject("apartment", apartment);
        modelAndView.addObject("pageTitle", APARTMENT_DETAILS);

        modelAndView.setViewName("details-apartment");
        return modelAndView;
    }
}
