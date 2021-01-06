package com.syn.domo.web.controller;

import com.syn.domo.model.ErrorResponse;
import com.syn.domo.model.binding.*;
import com.syn.domo.model.service.StaffServiceModel;
import com.syn.domo.model.service.UserServiceModel;
import com.syn.domo.model.view.StaffViewModel;
import com.syn.domo.service.StaffService;
import com.syn.domo.web.controller.namespace.StaffNamespace;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class StaffController implements StaffNamespace {

    private final StaffService staffService;
    private final ModelMapper modelMapper;

    @Autowired
    public StaffController(StaffService staffService, ModelMapper modelMapper) {
        this.staffService = staffService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<Set<StaffViewModel>> all() {

        Set<StaffViewModel> staff =
                this.staffService.getAll().stream()
                        .map(s -> this.modelMapper.map(s, StaffViewModel.class))
                        .collect(Collectors.toSet());

        return staff.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(staff);
    }

    @GetMapping("/{staffId}")
    public ResponseEntity<StaffViewModel> one(@PathVariable(value = "staffId") String staffId) {

        Optional<StaffServiceModel> staff = this.staffService.getOne(staffId);

        return staff.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(this.modelMapper.map(staff.get(), StaffViewModel.class));
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

    @PutMapping("/{staffId}")
    public ResponseEntity<?> edit(@PathVariable(value = "staffId") String staffId,
                                  @Valid @RequestBody StaffEditBindingModel staffEditBindingModel,
                                  BindingResult bindingResult,
                                  UriComponentsBuilder uriComponentsBuilder) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.unprocessableEntity()
                    .body(new ErrorResponse(bindingResult.getTarget(),
                            bindingResult.getAllErrors()));
        }

        this.staffService.edit(this.modelMapper.map(staffEditBindingModel, StaffServiceModel.class));

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .location(uriComponentsBuilder
                        .path(URI_STAFF + "/{staffId}")
                        .buildAndExpand(staffId)
                        .toUri()).build();
    }

    @DeleteMapping("/{staffId}")
    public ResponseEntity<?> delete(@PathVariable(value = "staffId") String staffId,
                                    UriComponentsBuilder uriComponentsBuilder) {

        this.staffService.delete(staffId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .location(uriComponentsBuilder
                        .path(URI_STAFF).build().toUri())
                .build();
    }

    @PutMapping("/{staffId}/assign-buildings")
    public ResponseEntity<?> assignBuildings(@PathVariable(value = "staffId") String staffId,
                                              @Valid @RequestBody
                                                      StaffAssignBuildingsBindingModel staffAssignBuildingsBindingModel,
                                              BindingResult bindingResult,
                                              UriComponentsBuilder uriComponentsBuilder) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.unprocessableEntity()
                    .body(new ErrorResponse(bindingResult.getTarget(),
                            bindingResult.getAllErrors()));
        }

        Set<String> buildingIds = staffAssignBuildingsBindingModel.getBuildings().stream()
                .map(BuildingIdBindingModel::getId)
                .collect(Collectors.toSet());

        this.staffService.assignBuildings(staffId, buildingIds);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .location(uriComponentsBuilder
                        .path(URI_STAFF + "/{staffId}")
                        .buildAndExpand(staffId)
                        .toUri()).build();
    }
}