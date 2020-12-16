package com.syn.domo.web.controller;

import com.syn.domo.model.binding.ResidentAddBindingModel;
import com.syn.domo.model.service.ResidentServiceModel;
import com.syn.domo.model.view.ApartmentViewModel;
import com.syn.domo.model.view.ResidentViewModel;
import com.syn.domo.service.ApartmentService;
import com.syn.domo.service.BuildingService;
import com.syn.domo.service.ResidentService;
import com.syn.domo.web.controller.namespace.ResidentsNamespace;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
public class ResidentsController implements ResidentsNamespace {

    private static final String MANAGE_RESIDENTS_TITLE = "Manage Residents";
    private static final String ADD_RESIDENT_TITLE = "Add New Resident";
    private static final String EDIT_RESIDENTS_TITLE = "Edit Residents";

    private final ResidentService residentService;
    private final ApartmentService apartmentService;
    private final BuildingService buildingService;
    private final ModelMapper modelMapper;

    @Autowired
    public ResidentsController(ResidentService residentService, ApartmentService apartmentService, BuildingService buildingService, ModelMapper modelMapper) {
        this.residentService = residentService;
        this.apartmentService = apartmentService;
        this.buildingService = buildingService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/manage")
    public ModelAndView manage(ModelAndView modelAndView) {
        Set<ResidentViewModel> residents =
                new LinkedHashSet<>(Collections.unmodifiableCollection(
                        this.residentService.getAllResidents().stream()
                        .map(residentServiceModel -> this.modelMapper.map(residentServiceModel, ResidentViewModel.class))
                        .collect(Collectors.toCollection(LinkedHashSet::new))));


        if (residents.size() > 0) {
            modelAndView.addObject("hasResidents", true);
            modelAndView.addObject("pageH3Title", EDIT_RESIDENTS_TITLE);
        }

        Set<String> apartmentNumbers =
                this.apartmentService.getAllApartmentsByBuildingId().stream()
                .map(apartmentServiceModel -> this.modelMapper.map(apartmentServiceModel, ApartmentViewModel.class))
                .map(ApartmentViewModel::getNumber)
                .collect(Collectors.toSet());


        modelAndView.addObject("residents", residents);
        modelAndView.addObject("apartmentNumbers", apartmentNumbers);
        modelAndView.addObject("pageTitle", MANAGE_RESIDENTS_TITLE);
        modelAndView.addObject("pageH2Title", ADD_RESIDENT_TITLE);
        modelAndView.setViewName("manage-residents");
        return modelAndView;
    }

    @PostMapping("/add")
    public ModelAndView addPost(@Valid @ModelAttribute("residentAddBindingModel")
                                                 ResidentAddBindingModel residentAddBindingModel,
                            BindingResult bindingResult, ModelAndView modelAndView) {

        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("redirect:/residents/manage");
        } else {
            this.residentService.register(
                    this.modelMapper.map(residentAddBindingModel, ResidentServiceModel.class));

            modelAndView.setViewName("redirect:/residents/manage");
        }

        return modelAndView;
    }

}
