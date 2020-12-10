package com.syn.domo.web.controllers;

import com.syn.domo.model.binding.ApartmentAddBindingModel;
import com.syn.domo.model.service.ApartmentServiceModel;
import com.syn.domo.service.ApartmentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
@RequestMapping("/apartments")
public class ApartmentController {
    private static final String ADD_APARTMENT_TITLE = "Add New Apartment";

    private final ApartmentService apartmentService;
    private final ModelMapper modelMapper;

    @Autowired
    public ApartmentController(ApartmentService apartmentService, ModelMapper modelMapper) {
        this.apartmentService = apartmentService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/add")
    public ModelAndView add(@ModelAttribute("apartment")
                                        ApartmentServiceModel apartment,
                            ModelAndView modelAndView) {

        modelAndView.addObject("pageTitle", ADD_APARTMENT_TITLE);
        modelAndView.setViewName("add-apartment");

        return modelAndView;
    }

    @PostMapping("/add")
    public ModelAndView addPost(@Valid @ModelAttribute("apartmentAddBindingModel")
                                            ApartmentAddBindingModel apartmentAddBindingModel,
                                BindingResult bindingResult, ModelAndView modelAndView,
                                RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("redirect:/apartments/add");
        } else {
            ApartmentServiceModel apartment =
                    this.apartmentService.add(
                            this.modelMapper.map(apartmentAddBindingModel, ApartmentServiceModel.class));
            redirectAttributes.addFlashAttribute("apartment", apartment);
            redirectAttributes.addFlashAttribute("alreadyExists",apartment.getId() == null);
            modelAndView.setViewName("redirect:/apartments/add");
        }

        return modelAndView;
    }
}
