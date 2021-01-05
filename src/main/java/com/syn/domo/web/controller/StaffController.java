package com.syn.domo.web.controller;

import com.syn.domo.model.ErrorResponse;
import com.syn.domo.model.binding.StaffAddBindingModel;
import com.syn.domo.model.service.StaffServiceModel;
import com.syn.domo.service.StaffService;
import com.syn.domo.web.controller.namespace.StaffNamespace;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;

import static com.syn.domo.web.controller.namespace.StaffNamespace.URI_STAFF;

@RestController
public class StaffController implements StaffNamespace {

    private final StaffService staffService;
    private final ModelMapper modelMapper;

    @Autowired
    public StaffController(StaffService staffService, ModelMapper modelMapper) {
        this.staffService = staffService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public ResponseEntity<?> add(@Valid @RequestBody StaffAddBindingModel staffAddBindingModel,
                                 BindingResult bindingResult,
                                 UriComponentsBuilder uriComponentsBuilder) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.unprocessableEntity()
                    .body(new ErrorResponse(bindingResult.getTarget(),
                            bindingResult.getAllErrors()));
        }

        String staffId = this.staffService
                        .add(this.modelMapper.map(staffAddBindingModel, StaffServiceModel.class))
                        .getId();

        return ResponseEntity.created(uriComponentsBuilder
                .path(URI_STAFF + "/{staffId}")
                .buildAndExpand(staffId)
                .toUri()).build();
    }
}
