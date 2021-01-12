package com.syn.domo.web.controller;

import com.syn.domo.model.view.ResponseModel;
import com.syn.domo.model.view.error.ErrorView;
import com.syn.domo.model.binding.ResidentBindingModel;
import com.syn.domo.model.service.ResidentServiceModel;
import com.syn.domo.model.view.ResidentViewModel;
import com.syn.domo.service.ResidentService;
import com.syn.domo.web.controller.namespace.ResidentsNamespace;
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
public class ResidentsController implements ResidentsNamespace {

    private final ResidentService residentService;
    private final ModelMapper modelMapper;

    @Autowired
    public ResidentsController(ResidentService residentService, ModelMapper modelMapper) {
        this.residentService = residentService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<Set<ResidentViewModel>> getAll(@PathVariable(value = "buildingId") String buildingId,
                                                         @PathVariable(value = "apartmentId") String apartmentId) {

        Set<ResidentViewModel> residents = this.residentService
                .getAll(buildingId, apartmentId)
                .stream()
                .map(r -> this.modelMapper.map(r, ResidentViewModel.class))
                .collect(Collectors.toSet());

        return residents.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(residents);
    }

    @GetMapping("/{residentId}")
    public ResponseEntity<ResidentViewModel> get(@PathVariable(value = "buildingId") String buildingId,
                                                 @PathVariable(value = "apartmentId") String apartmentId,
                                                 @PathVariable(value = "residentId") String residentId) {
        Optional<ResidentServiceModel> resident =
                this.residentService.get(buildingId, apartmentId, residentId);

        return resident.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(this.modelMapper.map(resident.get(), ResidentViewModel.class));
    }

    @PostMapping
    public ResponseEntity<?> add(@PathVariable(value = "buildingId") String buildingId,
                                 @PathVariable(value = "apartmentId") String apartmentId,
                                 @Valid @RequestBody ResidentBindingModel residentBindingModel,
                                 BindingResult bindingResult,
                                 UriComponentsBuilder uriComponentsBuilder) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.unprocessableEntity()
                    .body(new ResponseModel<>(residentBindingModel, bindingResult));
        }

        ResponseModel<ResidentServiceModel> responseModel = this.residentService
                .add(this.modelMapper.map(residentBindingModel, ResidentServiceModel.class),
                buildingId, apartmentId);

        return responseModel.hasErrors()
                ? ResponseEntity.unprocessableEntity().body(responseModel)
                : ResponseEntity.created(uriComponentsBuilder
                .path(URI_RESIDENTS + "/{residentId}")
                .buildAndExpand(buildingId, apartmentId, responseModel.getId())
                .toUri()).build();
    }

    @PutMapping("/{residentId}")
    public ResponseEntity<?> edit(@PathVariable(value = "buildingId") String buildingId,
                                  @PathVariable(value = "apartmentId") String apartmentId,
                                  @PathVariable(value = "residentId") String residentId,
                                  @Valid @RequestBody ResidentBindingModel residentBindingModel,
                                  BindingResult bindingResult, UriComponentsBuilder uriComponentsBuilder) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.unprocessableEntity()
                    .body(new ResponseModel<>(residentBindingModel, bindingResult));
        }

        ResponseModel<ResidentServiceModel> responseModel = this.residentService
                .edit(this.modelMapper.map(residentBindingModel, ResidentServiceModel.class),
                buildingId, apartmentId, residentId);

        return responseModel.hasErrors()
                ? ResponseEntity.unprocessableEntity().body(responseModel)
                : ResponseEntity.created(uriComponentsBuilder
                .path(URI_RESIDENTS + "/{residentId}")
                .buildAndExpand(buildingId, apartmentId, residentId)
                .toUri()).build();
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAll(@PathVariable(value = "buildingId") String buildingId,
                                       @PathVariable(value = "apartmentId") String apartmentId,
                                       UriComponentsBuilder uriComponentsBuilder) {

        this.residentService.deleteAll(buildingId, apartmentId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .location(uriComponentsBuilder
                        .path(URI_RESIDENTS)
                        .buildAndExpand(buildingId, apartmentId)
                        .toUri())
                .build();
    }

    @DeleteMapping("/{residentId}")
    public ResponseEntity<?> delete(@PathVariable(value = "buildingId") String buildingId,
                                    @PathVariable(value = "apartmentId") String apartmentId,
                                    @PathVariable(value = "residentId") String residentId,
                                    UriComponentsBuilder uriComponentsBuilder) {

        this.residentService.delete(buildingId, apartmentId, residentId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .location(uriComponentsBuilder
                        .path(URI_RESIDENTS)
                        .buildAndExpand(buildingId, apartmentId)
                        .toUri())
                .build();
    }
}
