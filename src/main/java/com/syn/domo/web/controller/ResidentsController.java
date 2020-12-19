package com.syn.domo.web.controller;

import com.syn.domo.model.binding.ResidentAddBindingModel;
import com.syn.domo.model.service.ResidentServiceModel;
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
import org.springframework.web.bind.annotation.PathVariable;
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
    private static final String RESIDENT_DETAILS = "Resident Details";

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

    @GetMapping("/")
    public ModelAndView manage(@PathVariable(value = "buildingId") String buildingId,
                               @PathVariable(value = "apartmentId") String apartmentId,
                               ModelAndView modelAndView) {

        Set<ResidentViewModel> residents =
                new LinkedHashSet<>(Collections.unmodifiableCollection(
                        this.residentService.getAllResidentsByApartmentId(apartmentId).stream()
                        .map(residentServiceModel -> this.modelMapper.map(residentServiceModel, ResidentViewModel.class))
                        .collect(Collectors.toCollection(LinkedHashSet::new))));

        boolean hasResidents = residents.size() > 0;
        modelAndView.addObject("hasResidents", hasResidents);

        if (hasResidents) {
            modelAndView.addObject("pageH3Title", EDIT_RESIDENTS_TITLE);
        }

        modelAndView.addObject("buildingName", this.buildingService.getBuildingName(buildingId));
        modelAndView.addObject("apartmentNumber", this.apartmentService.getById(apartmentId).getNumber());
        modelAndView.addObject("residents", residents);
        modelAndView.addObject("pageTitle", MANAGE_RESIDENTS_TITLE);
        modelAndView.addObject("pageH2Title", ADD_RESIDENT_TITLE);
        modelAndView.setViewName("manage-residents");
        return modelAndView;
    }

    @PostMapping("/")
    public ModelAndView add(@PathVariable(value = "buildingId") String buildingId,
                            @PathVariable(value = "apartmentId") String apartmentId,
                            @Valid @ModelAttribute("residentAddBindingModel")
                                        ResidentAddBindingModel residentAddBindingModel,
                            BindingResult bindingResult, ModelAndView modelAndView) {

        if (bindingResult.hasErrors()) {
            modelAndView.setViewName("redirect:/buildings/" + buildingId +
                    "/apartments/" + apartmentId + "/residents/");
        } else {
            this.residentService.add(
                    this.modelMapper.map(residentAddBindingModel, ResidentServiceModel.class));

            modelAndView.setViewName("redirect:/buildings/" + buildingId +
                    "/apartments/" + apartmentId + "/residents/");
        }

        return modelAndView;
    }

    @GetMapping("/{residentId}")
    public ModelAndView details(@PathVariable(value = "buildingId") String buildingId,
                                @PathVariable(value = "apartmentId") String apartmentId,
                                @PathVariable(value = "residentId") String residentId,
                                ModelAndView modelAndView) {
        ResidentViewModel resident =
                this.modelMapper.map(this.residentService.getById(residentId), ResidentViewModel.class);

        modelAndView.addObject("resident", resident);
        modelAndView.addObject("buildingName",
                this.buildingService.getBuildingName(buildingId));
        modelAndView.addObject("pageTitle", RESIDENT_DETAILS);
        modelAndView.setViewName("details-resident");
        return modelAndView;
    }

}
