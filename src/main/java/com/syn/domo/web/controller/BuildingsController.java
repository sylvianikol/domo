package com.syn.domo.web.controller;

import com.syn.domo.model.view.ResponseModel;
import com.syn.domo.model.binding.BuildingBindingModel;
import com.syn.domo.model.service.BuildingServiceModel;
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
import org.springframework.data.domain.Pageable;

import static com.syn.domo.common.ResponseStatusMessages.DELETE_FAILED;
import static com.syn.domo.common.ResponseStatusMessages.DELETE_SUCCESSFUL;

//@CrossOrigin("http://host")
@RestController
public class BuildingsController implements BuildingsNamespace {

    private final BuildingService buildingService;
    private final ModelMapper modelMapper;

    @Autowired
    public BuildingsController(BuildingService buildingService,
                               ModelMapper modelMapper) {
        this.buildingService = buildingService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/all")
    public ResponseEntity<Set<BuildingViewModel>> getAll(Pageable pageable) {

        Set<BuildingViewModel> buildings = this.buildingService.getAll(pageable).stream()
                .map(buildingServiceModel -> this.modelMapper
                        .map(buildingServiceModel, BuildingViewModel.class))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        return buildings.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok().body(buildings);
    }

    @GetMapping("/{buildingId}")
    public ResponseEntity<BuildingViewModel> get(@PathVariable(value = "buildingId") String buildingId) {

        Optional<BuildingServiceModel> buildingServiceModel =
                this.buildingService.get(buildingId);

        return buildingServiceModel.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok().body(
                this.modelMapper.map(buildingServiceModel.get(), BuildingViewModel.class));

    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@Valid @RequestBody BuildingBindingModel buildingBindingModel,
                                 BindingResult bindingResult,
                                 UriComponentsBuilder uriComponentsBuilder) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.unprocessableEntity()
                    .body(new ResponseModel<>(buildingBindingModel, bindingResult));
        }

        ResponseModel<BuildingServiceModel> responseModel = this.buildingService.add(
                this.modelMapper.map(buildingBindingModel, BuildingServiceModel.class));

        return responseModel.hasErrors()
                ? ResponseEntity.unprocessableEntity().body(responseModel)
                : ResponseEntity.created(uriComponentsBuilder.path(URI_BUILDINGS + "/{buildingId}")
                .buildAndExpand(responseModel.getId())
                .toUri()).build();
    }

    @PutMapping("/{buildingId}")
    public ResponseEntity<?> edit(@PathVariable(value = "buildingId") String buildingId,
                                  @Valid @RequestBody BuildingBindingModel buildingBindingModel,
                                  BindingResult bindingResult,
                                  UriComponentsBuilder uriComponentsBuilder) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.unprocessableEntity()
                    .body(new ResponseModel<>(buildingBindingModel, bindingResult));
        }

        ResponseModel<BuildingServiceModel> responseModel =
                this.buildingService.edit(this.modelMapper.map(
                buildingBindingModel, BuildingServiceModel.class), buildingId);

        return responseModel.hasErrors()
                ? ResponseEntity.unprocessableEntity().body(responseModel)
                : ResponseEntity.status(HttpStatus.NO_CONTENT)
                .location(uriComponentsBuilder.path(URI_BUILDINGS + "/{buildingId}")
                        .buildAndExpand(buildingId)
                        .toUri()).build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteAll() {
        int result = this.buildingService.deleteAll();
        return result == 0
                ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(DELETE_FAILED)
                : ResponseEntity.ok().body(String.format(DELETE_SUCCESSFUL, result, "buildings"));
    }

    @DeleteMapping("/{buildingId}")
    public ResponseEntity<?> delete(@PathVariable(value = "buildingId") String buildingId,
                                                          UriComponentsBuilder uriComponentsBuilder) {

        this.buildingService.delete(buildingId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .location(uriComponentsBuilder.path(URI_BUILDINGS).build().toUri())
                .build();
    }

    @PutMapping("/{buildingId}/assign")
    public ResponseEntity<?> assignStaff(@RequestParam(name = "staffIds") Set<String> staffIds,
                                         @PathVariable(value = "buildingId") String buildingId,
                                         UriComponentsBuilder uriComponentsBuilder) {

        this.buildingService.assignStaff(buildingId, staffIds);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .location(uriComponentsBuilder
                        .path(URI_BUILDINGS + "/{buildingId}")
                        .buildAndExpand(buildingId)
                        .toUri()).build();
    }
}
