package com.syn.domo.web.controllers;

import com.syn.domo.model.binding.ApartmentAddBindingModel;
import com.syn.domo.model.service.ApartmentServiceModel;
import com.syn.domo.service.ApartmentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
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
    public ModelAndView add(ModelAndView modelAndView) {
        modelAndView.addObject("pageTitle", ADD_APARTMENT_TITLE);
        modelAndView.setViewName("add-apartment");
        return modelAndView;
    }

    @PostMapping("/add")
    public ModelAndView addPost(@Valid @ModelAttribute("apartmentAddBindingModel")
                                            ApartmentAddBindingModel apartmentAddBindingModel,
                                BindingResult bindingResult, ModelAndView modelAndView, HttpSession httpSession) {

        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("redirect:/apartments/add");
        } else {
            ApartmentServiceModel serviceModel =
                    this.apartmentService.add(
                            this.modelMapper.map(apartmentAddBindingModel, ApartmentServiceModel.class));

            if (serviceModel.getId() == null) {
                modelAndView.setViewName("redirect:/apartments/add");
            } else {
                modelAndView.setViewName("redirect:/");
            }
        }

        return modelAndView;
    }
}
