package com.syn.domo.web.controller;

import com.syn.domo.exception.BuildingExistsException;
import com.syn.domo.exception.BuildingNotFoundException;
import com.syn.domo.exception.FloorNotValidException;
import com.syn.domo.exception.SameDataException;
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

        Optional<BuildingServiceModel> buildingServiceModel = this.buildingService.getById(buildingId);
        if (buildingServiceModel.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok().body(
                this.modelMapper.map(buildingServiceModel.get(), BuildingViewModel.class));

    }

    @PostMapping
    public ResponseEntity<BuildingAddViewModel> add(@Valid @RequestBody BuildingAddBindingModel buildingAddBindingModel,
                                                    BindingResult bindingResult,
                                                    UriComponentsBuilder uriComponentsBuilder) {
        if (bindingResult.hasErrors()) {
            BuildingAddViewModel buildingAddViewModel =
                    this.modelMapper.map(buildingAddBindingModel, BuildingAddViewModel.class);
            return ResponseEntity.unprocessableEntity()
                    .body(buildingAddViewModel);
        }

        String buildingId;

        try {

            buildingId = this.buildingService.add(
                    this.modelMapper.map(buildingAddBindingModel, BuildingServiceModel.class)).getId();

        } catch (BuildingExistsException ex) {

            Optional<BuildingServiceModel> building = this.buildingService
                    .getOne(buildingAddBindingModel.getName().trim(),
                            buildingAddBindingModel.getAddress().trim(),
                            buildingAddBindingModel.getNeighbourhood().trim());

            return building.map(serviceModel ->
                    ResponseEntity.status(HttpStatus.CONFLICT)
                            .body(this.modelMapper.map(serviceModel, BuildingAddViewModel.class)))
                    .orElseGet(() -> ResponseEntity.notFound().build());

        }

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
                    .body(this.modelMapper.map(buildingEditBindingModel,
                            BuildingEditViewModel.class));
        }

        try {
            this.buildingService.edit(this.modelMapper.map(
                    buildingEditBindingModel, BuildingServiceModel.class)
                    , buildingId);
        } catch (BuildingExistsException e) {
            Optional<BuildingServiceModel> building = this.buildingService
                    .getOne(buildingEditBindingModel.getName().trim(),
                            buildingEditBindingModel.getAddress().trim(),
                            buildingEditBindingModel.getNeighbourhood().trim());

            return building.map(serviceModel ->
                    ResponseEntity.status(HttpStatus.CONFLICT)
                            .body(this.modelMapper.map(serviceModel, BuildingEditViewModel.class)))
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (SameDataException | FloorNotValidException e) {
            return ResponseEntity.unprocessableEntity()
                    .body(this.modelMapper.map(buildingEditBindingModel,
                            BuildingEditViewModel.class));
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
