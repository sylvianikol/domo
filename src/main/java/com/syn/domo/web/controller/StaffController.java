package com.syn.domo.web.controller;

import com.syn.domo.service.StaffService;
import com.syn.domo.web.controller.namespace.StaffNamespace;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StaffController implements StaffNamespace {

    private final StaffService staffService;
    private final ModelMapper modelMapper;

    @Autowired
    public StaffController(StaffService staffService, ModelMapper modelMapper) {
        this.staffService = staffService;
        this.modelMapper = modelMapper;
    }


}
