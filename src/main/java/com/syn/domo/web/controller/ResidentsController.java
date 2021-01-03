package com.syn.domo.web.controller;

import com.syn.domo.model.ErrorResponse;
import com.syn.domo.model.binding.UserAddBindingModel;
import com.syn.domo.model.service.UserServiceModel;
import com.syn.domo.service.ResidentService;
import com.syn.domo.web.controller.namespace.ResidentsNamespace;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;

@RestController
public class ResidentsController implements ResidentsNamespace {

    private final ResidentService residentService;
    private final ModelMapper modelMapper;

    @Autowired
    public ResidentsController(ResidentService residentService, ModelMapper modelMapper) {
        this.residentService = residentService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public ResponseEntity<?> add(@PathVariable(value = "buildingId") String buildingId,
                                 @PathVariable(value = "apartmentId") String apartmentId,
                                 @Valid @RequestBody UserAddBindingModel userAddBindingModel,
                                 BindingResult bindingResult, UriComponentsBuilder uriComponentsBuilder) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.unprocessableEntity()
                    .body(new ErrorResponse(bindingResult.getTarget(),
                            bindingResult.getAllErrors()));
        }

        String residentId = this.residentService.add(
                this.modelMapper.map(userAddBindingModel, UserServiceModel.class),
                buildingId, apartmentId).getId();

        return ResponseEntity.created(uriComponentsBuilder
                .path(URI_RESIDENTS + "/{residentId}")
                .buildAndExpand(buildingId, apartmentId, residentId)
                .toUri()).build();
    }
}
