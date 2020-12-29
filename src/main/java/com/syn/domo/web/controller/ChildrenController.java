package com.syn.domo.web.controller;

import com.syn.domo.model.ErrorResponse;
import com.syn.domo.model.binding.ChildAddBindingModel;
import com.syn.domo.model.binding.ChildEditBindingModel;
import com.syn.domo.model.service.ChildServiceModel;
import com.syn.domo.model.view.ChildViewModel;
import com.syn.domo.service.ApartmentService;
import com.syn.domo.service.BuildingService;
import com.syn.domo.service.ChildService;
import com.syn.domo.web.controller.namespace.ChildrenNamespace;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
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

    @GetMapping
    public ResponseEntity<Set<ChildViewModel>> all(@PathVariable(value = "buildingId") String buildingId,
                                                   @PathVariable(value = "apartmentId") String apartmentId) {
        Set<ChildViewModel> children = this.childService
                .getAllByApartmentIdAndBuildingId(buildingId, apartmentId)
                .stream()
                .map(c -> this.modelMapper.map(c, ChildViewModel.class))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        return children.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(children);
    }

    @PostMapping
    public ResponseEntity<?> add(@PathVariable(value = "buildingId") String buildingId,
                                 @PathVariable(value = "apartmentId") String apartmentId,
                                 @Valid @RequestBody ChildAddBindingModel childAddBindingModel,
                                 BindingResult bindingResult, UriComponentsBuilder uriComponentsBuilder) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.unprocessableEntity()
                    .body(new ErrorResponse(bindingResult.getTarget(),
                            bindingResult.getAllErrors()));
        }

        ChildServiceModel childServiceModel =
                this.modelMapper.map(childAddBindingModel, ChildServiceModel.class);
        String childId = this.childService.add(childServiceModel, buildingId, apartmentId).getId();

        return ResponseEntity.created(uriComponentsBuilder
                .path(URI_CHILDREN + "/{child_id}")
                .buildAndExpand(buildingId, apartmentId, childId)
                .toUri()).build();
    }

    @PutMapping("/{childId}")
    public ResponseEntity<?> edit(@PathVariable(value = "buildingId") String buildingId,
                                  @PathVariable(value = "apartmentId") String apartmentId,
                                  @PathVariable(value = "childId") String childId,
                                  @Valid @RequestBody ChildEditBindingModel childEditBindingModel,
                                  BindingResult bindingResult, UriComponentsBuilder uriComponentsBuilder) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.unprocessableEntity()
                    .body(new ErrorResponse(bindingResult.getTarget(),
                            bindingResult.getAllErrors()));
        }

        this.childService.edit(this.modelMapper.map(childEditBindingModel, ChildServiceModel.class),
                buildingId, apartmentId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .location(uriComponentsBuilder.path(URI_CHILDREN + "/{childId}")
                        .buildAndExpand(buildingId, apartmentId, childId)
                        .toUri()).build();
    }

    @DeleteMapping("/{childId}")
    public ResponseEntity<?> delete(@PathVariable(value = "buildingId") String buildingId,
                                    @PathVariable(value = "apartmentId") String apartmentId,
                                    @PathVariable(value = "childId") String childId,
                                    UriComponentsBuilder uriComponentsBuilder) {

        this.childService.delete(childId, buildingId, apartmentId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .location(uriComponentsBuilder
                        .path(URI_CHILDREN)
                        .buildAndExpand(buildingId, apartmentId)
                        .toUri()).build();
    }
}
