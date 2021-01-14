package com.syn.domo.web.controller;

import com.syn.domo.model.view.ResponseModel;
import com.syn.domo.model.binding.*;
import com.syn.domo.model.service.StaffServiceModel;
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

import static com.syn.domo.common.DefaultParamValues.EMPTY_URL;

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
    public ResponseEntity<Set<StaffViewModel>> getAll(@RequestParam(required = false, defaultValue = EMPTY_URL,
                                                                 name = "buildingId") String buildingId) {

        Set<StaffViewModel> staff = this.staffService.getAll(buildingId).stream()
                .map(s -> this.modelMapper.map(s, StaffViewModel.class))
                .collect(Collectors.toSet());

        return staff.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(staff);
    }

    @GetMapping("/{staffId}")
    public ResponseEntity<StaffViewModel> get(@PathVariable(value = "staffId") String staffId) {

        Optional<StaffServiceModel> staff = this.staffService.get(staffId);

        return staff.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(this.modelMapper.map(staff.get(), StaffViewModel.class));
    }

    @PostMapping
    public ResponseEntity<?> add(@Valid @RequestBody StaffBindingModel staffBindingModel,
                                 BindingResult bindingResult,
                                 UriComponentsBuilder uriComponentsBuilder) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.unprocessableEntity()
                    .body(new ResponseModel<>(staffBindingModel, bindingResult));
        }

        ResponseModel<StaffServiceModel> responseModel =
                this.staffService.add(this.modelMapper.map(staffBindingModel, StaffServiceModel.class));

        return responseModel.hasErrors()
                ? ResponseEntity.unprocessableEntity().body(responseModel)
                : ResponseEntity.created(uriComponentsBuilder.path(URI_STAFF + "/{staffId}")
                .buildAndExpand(responseModel.getId()).toUri())
                .build();
    }

    @PutMapping("/{staffId}")
    public ResponseEntity<?> edit(@PathVariable(value = "staffId") String staffId,
                                  @Valid @RequestBody StaffBindingModel staffBindingModel,
                                  BindingResult bindingResult,
                                  UriComponentsBuilder uriComponentsBuilder) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.unprocessableEntity()
                    .body(new ResponseModel<>(staffBindingModel, bindingResult));
        }

        ResponseModel<StaffServiceModel> responseModel = this.staffService
                .edit(this.modelMapper.map(staffBindingModel, StaffServiceModel.class), staffId);

        return responseModel.hasErrors()
                ? ResponseEntity.unprocessableEntity().body(responseModel)
                : ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .location(uriComponentsBuilder.path(URI_STAFF + "/{staffId}")
                    .buildAndExpand(staffId).toUri())
                .build();
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAll(@RequestParam(required = false, defaultValue = EMPTY_URL,
                                                     name = "buildingId") String buildingId,
                                       UriComponentsBuilder uriComponentsBuilder) {

        this.staffService.deleteAll(buildingId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .location(uriComponentsBuilder.path(URI_STAFF).build().toUri())
                .build();
    }

    @DeleteMapping("/{staffId}")
    public ResponseEntity<?> delete(@PathVariable(value = "staffId") String staffId,
                                    UriComponentsBuilder uriComponentsBuilder) {

        this.staffService.delete(staffId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .location(uriComponentsBuilder.path(URI_STAFF).build().toUri())
                .build();
    }

    @PutMapping("/{staffId}/assign")
    public ResponseEntity<?> assignBuildings(@RequestParam(name = "buildingIds") Set<String> buildingIds,
                                             @PathVariable(value = "staffId") String staffId,
                                             UriComponentsBuilder uriComponentsBuilder) {

        this.staffService.assignBuildings(staffId, buildingIds);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .location(uriComponentsBuilder.path(URI_STAFF + "/{staffId}")
                        .buildAndExpand(staffId).toUri())
                .build();
    }
}
