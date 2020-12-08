package com.syn.domo.web.controllers;

import com.syn.domo.model.binding.ResidentAddBindingModel;
import com.syn.domo.model.service.ResidentServiceModel;
import com.syn.domo.model.view.ApartmentViewModel;
import com.syn.domo.service.ApartmentService;
import com.syn.domo.service.ResidentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/residents")
public class ResidentController {

    private static final String ADD_RESIDENT_TITLE = "Add New Resident";

    private final ResidentService residentService;
    private final ApartmentService apartmentService;
    private final ModelMapper modelMapper;

    @Autowired
    public ResidentController(ResidentService residentService, ApartmentService apartmentService, ModelMapper modelMapper) {
        this.residentService = residentService;
        this.apartmentService = apartmentService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/add")
    public ModelAndView add(ModelAndView modelAndView) {
        Set<ApartmentViewModel> apartmentViewModels = this.apartmentService.getAllApartments().stream()
                .map(apartmentServiceModel -> this.modelMapper.map(apartmentServiceModel, ApartmentViewModel.class))
                .collect(Collectors.toSet());

        modelAndView.addObject("apartmentNumbers", apartmentViewModels);
        modelAndView.addObject("pageTitle", ADD_RESIDENT_TITLE);

        modelAndView.setViewName("add-resident");
        return modelAndView;
    }

    @PostMapping("/add")
    public ModelAndView addPost(@Valid @ModelAttribute("residentAddBindingModel")
                                                 ResidentAddBindingModel residentAddBindingModel,
                            BindingResult bindingResult, ModelAndView modelAndView) {

        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("redirect:/residents/add");
        } else {

            this.residentService.register(
                    this.modelMapper.map(residentAddBindingModel, ResidentServiceModel.class));

            modelAndView.setViewName("redirect:/residents/login");
        }

        return modelAndView;
    }

}
