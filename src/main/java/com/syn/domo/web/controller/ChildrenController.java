package com.syn.domo.web.controller;

import com.syn.domo.model.binding.ChildAddBindingModel;
import com.syn.domo.model.service.ChildServiceModel;
import com.syn.domo.model.view.ChildViewModel;
import com.syn.domo.service.ApartmentService;
import com.syn.domo.service.BuildingService;
import com.syn.domo.service.ChildService;
import com.syn.domo.web.controller.namespace.ChildrenNamespace;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class ChildrenController implements ChildrenNamespace {

    private final ChildService childService;
    private final BuildingService buildingService;
    private final ApartmentService apartmentService;
    private final ModelMapper modelMapper;

    @Autowired
    public ChildrenController(ChildService childService, BuildingService buildingService, ApartmentService apartmentService, ModelMapper modelMapper) {
        this.childService = childService;
        this.buildingService = buildingService;
        this.apartmentService = apartmentService;
        this.modelMapper = modelMapper;
    }


}
