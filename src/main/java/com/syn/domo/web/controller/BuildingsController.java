package com.syn.domo.web.controller;

import com.syn.domo.exception.BuildingExistsException;
import com.syn.domo.exception.BuildingNotFoundException;
import com.syn.domo.model.binding.BuildingAddBindingModel;
import com.syn.domo.model.binding.BuildingEditBindingModel;
import com.syn.domo.model.service.BuildingServiceModel;
import com.syn.domo.model.view.BuildingAddViewModel;
import com.syn.domo.model.view.BuildingDeleteViewModel;
import com.syn.domo.model.view.BuildingEditViewModel;
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
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
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

    @GetMapping("")
    public ResponseEntity<Set<BuildingViewModel>> getAll() {
        Set<BuildingViewModel> buildings = this.buildingService.getAllBuildings().stream()
                .map(buildingServiceModel -> this.modelMapper.map(buildingServiceModel, BuildingViewModel.class))
                .collect(Collectors.toCollection(LinkedHashSet::new));
        return buildings.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok().body(buildings);
    }

    @GetMapping("/{buildingId}")
    public ResponseEntity<BuildingViewModel> get(@PathVariable(value = "buildingId") String buildingId) {

        Optional<BuildingServiceModel> buildingServiceModel = this.buildingService.getOptById(buildingId);
        if (buildingServiceModel.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok().body(
                    this.modelMapper.map(buildingServiceModel.get(), BuildingViewModel.class));
        }
    }

    @PostMapping("")
    public ResponseEntity<BuildingAddViewModel> add(@Valid @RequestBody BuildingAddBindingModel buildingAddBindingModel,
                                                    BindingResult bindingResult,
                                                    UriComponentsBuilder uriComponentsBuilder) {
        if (bindingResult.hasErrors()) {
            BuildingAddViewModel buildingAddViewModel =
                    this.modelMapper.map(bindingResult.getTarget(), BuildingAddViewModel.class);
            return ResponseEntity.unprocessableEntity()
                    .body(buildingAddViewModel);
        }

        String buildingId = this.buildingService.add(
                this.modelMapper.map(buildingAddBindingModel, BuildingServiceModel.class)).getId();

        return ResponseEntity.created(uriComponentsBuilder
                .path("/buildings/{buildingId}")
                .buildAndExpand(buildingId)
                .toUri()).build();
    }

    @PutMapping("/{buildingId}")
    public ResponseEntity<BuildingEditViewModel> edit(@PathVariable(value = "buildingId") String buildingId,
                                                      @Valid @RequestBody BuildingEditBindingModel buildingEditBindingModel,
                                                      BindingResult bindingResult,
                                                      UriComponentsBuilder uriComponentsBuilder) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.unprocessableEntity()
                    .body(this.modelMapper.map(bindingResult.getTarget(),
                            BuildingEditViewModel.class));
        }

        try {
            this.buildingService.edit(this.modelMapper.map(
                    buildingEditBindingModel, BuildingServiceModel.class)
                    , buildingId);
        } catch (BuildingExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(this.modelMapper.map(this.buildingService
                            .getBuilding(buildingEditBindingModel.getName().trim(),
                                    buildingEditBindingModel.getAddress().trim(),
                                    buildingEditBindingModel.getNeighbourhood().trim()), BuildingEditViewModel.class));
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .location(uriComponentsBuilder
                        .path("/buildings/{buildingId}")
                        .buildAndExpand(buildingId)
                        .toUri()).build();
    }

    @DeleteMapping("/{buildingId}")
    public ResponseEntity<BuildingDeleteViewModel> delete(@PathVariable(value = "buildingId") String buildingId,
                                                          UriComponentsBuilder uriComponentsBuilder) {

        BuildingServiceModel buildingServiceModel;

        try {
            buildingServiceModel = this.buildingService.delete(buildingId);
        } catch (BuildingNotFoundException e) {
            return ResponseEntity.notFound().build();
        }

        BuildingDeleteViewModel deletedBuilding = this.modelMapper
                .map(buildingServiceModel, BuildingDeleteViewModel.class);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .location(uriComponentsBuilder
                        .path("/buildings")
                        .build()
                        .toUri())
                .body(deletedBuilding);
    }
}
