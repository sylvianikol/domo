package com.syn.domo.web.controller;

import com.syn.domo.service.FeeService;
import com.syn.domo.web.controller.namespace.FeesNamespace;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
public class FeesController implements FeesNamespace {

    private final FeeService feeService;
    private final ModelMapper modelMapper;

    @Autowired
    public FeesController(FeeService feeService, ModelMapper modelMapper) {
        this.feeService = feeService;
        this.modelMapper = modelMapper;
    }

    public ResponseEntity<Set<?>> all() {
        return null;
    }
}
