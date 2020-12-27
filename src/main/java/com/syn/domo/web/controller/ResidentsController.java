package com.syn.domo.web.controller;

import com.syn.domo.model.ErrorResponse;
import com.syn.domo.model.binding.ResidentAddBindingModel;
import com.syn.domo.model.binding.ResidentEditBindingModel;
import com.syn.domo.model.service.ResidentServiceModel;
import com.syn.domo.model.view.ResidentViewModel;
import com.syn.domo.service.ApartmentService;
import com.syn.domo.service.BuildingService;
import com.syn.domo.service.ResidentService;
import com.syn.domo.web.controller.namespace.ResidentsNamespace;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.syn.domo.web.controller.namespace.ResidentsNamespace.URI_RESIDENTS;

@RestController
public class ResidentsController implements ResidentsNamespace {
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

    @GetMapping
    public ResponseEntity<Set<ResidentViewModel>> all(@PathVariable(value = "buildingId") String buildingId,
                                                      @PathVariable(value = "apartmentId") String apartmentId) {
        Set<ResidentViewModel> residents =
                this.residentService.getAllByApartmentId(apartmentId).stream()
                .map(r -> this.modelMapper.map(r, ResidentViewModel.class))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        return residents.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(residents);
    }

    @GetMapping("/{residentId}")
    public ResponseEntity<ResidentViewModel> one(@PathVariable(value = "buildingId") String buildingId,
                                                 @PathVariable(value = "apartmentId") String apartmentId,
                                                 @PathVariable(value = "residentId") String residentId) {

        return this.residentService.getById(residentId)
                .filter(r -> r.getApartment().getId().equals(apartmentId)
                        && r.getApartment().getBuilding().getId().equals(buildingId))
                .map(r -> ResponseEntity.ok()
                        .body(this.modelMapper.map(r, ResidentViewModel.class)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> add(@PathVariable(value = "buildingId") String buildingId,
                                 @PathVariable(value = "apartmentId") String apartmentId,
                                 @Valid @RequestBody ResidentAddBindingModel residentAddBindingModel,
                                 BindingResult bindingResult, UriComponentsBuilder uriComponentsBuilder) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.unprocessableEntity()
                    .body(new ErrorResponse(bindingResult.getTarget(),
                            bindingResult.getAllErrors()));
        }

        String residentId = this.residentService.add(
                this.modelMapper.map(residentAddBindingModel, ResidentServiceModel.class),
                buildingId, apartmentId).getId();

        return ResponseEntity.created(uriComponentsBuilder
                .path(URI_RESIDENTS + "/{residentId}")
                .buildAndExpand(buildingId, apartmentId, residentId)
                .toUri()).build();
    }

    @PutMapping("/{residentId}")
    public ResponseEntity<?> edit(@PathVariable(value = "buildingId") String buildingId,
                                  @PathVariable(value = "apartmentId") String apartmentId,
                                  @PathVariable(value = "residentId") String residentId,
                                  @Valid @RequestBody ResidentEditBindingModel residentEditBindingModel,
                                  BindingResult bindingResult, UriComponentsBuilder uriComponentsBuilder) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.unprocessableEntity()
                    .body(new ErrorResponse(bindingResult.getTarget(),
                            bindingResult.getAllErrors()));
        }

        this.residentService.edit(this.modelMapper.map(residentEditBindingModel, ResidentServiceModel.class),
                buildingId, apartmentId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .location(uriComponentsBuilder
                        .path(URI_RESIDENTS + "/{residentId}")
                        .buildAndExpand(buildingId, apartmentId, residentId)
                        .toUri()).build();
    }

    @DeleteMapping("/{residentId}")
    public ResponseEntity<?> delete(@PathVariable(value = "buildingId") String buildingId,
                                    @PathVariable(value = "apartmentId") String apartmentId,
                                    @PathVariable(value = "residentId") String residentId,
                                    UriComponentsBuilder uriComponentsBuilder) {

        this.residentService.delete(residentId, buildingId, apartmentId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .location(uriComponentsBuilder
                        .path(URI_RESIDENTS)
                        .buildAndExpand(buildingId)
                        .toUri())
                .build();
    }
}
