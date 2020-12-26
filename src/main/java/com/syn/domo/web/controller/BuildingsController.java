package com.syn.domo.web.controller;

import com.syn.domo.model.ErrorResponse;
import com.syn.domo.model.binding.BuildingAddBindingModel;
import com.syn.domo.model.binding.BuildingEditBindingModel;
import com.syn.domo.model.service.BuildingServiceModel;
import com.syn.domo.model.view.BuildingDeleteViewModel;
import com.syn.domo.model.view.BuildingViewModel;
import com.syn.domo.service.BuildingService;
import com.syn.domo.web.controller.namespace.BuildingsNamespace;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class BuildingsController implements BuildingsNamespace {

    private final BuildingService buildingService;
    private final ModelMapper modelMapper;

    @Autowired
    public BuildingsController(BuildingService buildingService, ModelMapper modelMapper) {
        this.buildingService = buildingService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<Set<BuildingViewModel>> all() {
        Set<BuildingViewModel> buildings = this.buildingService.getAll().stream()
                .map(buildingServiceModel -> this.modelMapper.map(buildingServiceModel, BuildingViewModel.class))
                .collect(Collectors.toCollection(LinkedHashSet::new));
        return buildings.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok().body(buildings);
    }

    @GetMapping("/{buildingId}")
    public ResponseEntity<BuildingViewModel> one(@PathVariable(value = "buildingId") String buildingId) {

        Optional<BuildingServiceModel> buildingServiceModel =
                this.buildingService.getById(buildingId);

        return buildingServiceModel.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok().body(
                this.modelMapper.map(buildingServiceModel.get(), BuildingViewModel.class));

    }

    @PostMapping
    public ResponseEntity<?> add(@Valid @RequestBody BuildingAddBindingModel buildingAddBindingModel,
                                                    BindingResult bindingResult,
                                                    UriComponentsBuilder uriComponentsBuilder) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.unprocessableEntity()
                    .body(new ErrorResponse(bindingResult.getTarget(),
                            bindingResult.getAllErrors()));
        }

        String buildingId = this.buildingService.add(
                this.modelMapper.map(buildingAddBindingModel, BuildingServiceModel.class)).getId();

        return ResponseEntity.created(uriComponentsBuilder
                .path("/buildings/{buildingId}")
                .buildAndExpand(buildingId)
                .toUri()).build();
    }

    @PutMapping("/{buildingId}")
    public ResponseEntity<?> edit(@PathVariable(value = "buildingId") String buildingId,
                                                      @Valid @RequestBody BuildingEditBindingModel buildingEditBindingModel,
                                                      BindingResult bindingResult,
                                                      UriComponentsBuilder uriComponentsBuilder) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.unprocessableEntity()
                    .body(new ErrorResponse(bindingResult.getTarget(),
                            bindingResult.getAllErrors()));
        }

        this.buildingService.edit(this.modelMapper.map(
                buildingEditBindingModel, BuildingServiceModel.class)
                , buildingId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .location(uriComponentsBuilder
                        .path("/buildings/{buildingId}")
                        .buildAndExpand(buildingId)
                        .toUri()).build();
    }

    @DeleteMapping("/{buildingId}")
    public ResponseEntity<BuildingDeleteViewModel> delete(@PathVariable(value = "buildingId") String buildingId,
                                                          UriComponentsBuilder uriComponentsBuilder) {

        this.buildingService.delete(buildingId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .location(uriComponentsBuilder
                        .path("/buildings")
                        .build()
                        .toUri())
                .build();
    }
}
