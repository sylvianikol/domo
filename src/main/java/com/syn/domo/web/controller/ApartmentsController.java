package com.syn.domo.web.controller;

import com.syn.domo.model.ErrorResponse;
import com.syn.domo.model.binding.ApartmentBindingModel;
import com.syn.domo.model.service.ApartmentServiceModel;
import com.syn.domo.model.view.ApartmentErrorView;
import com.syn.domo.model.view.ApartmentViewModel;
import com.syn.domo.service.ApartmentService;
import com.syn.domo.web.controller.namespace.ApartmentsNamespace;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class ApartmentsController implements ApartmentsNamespace {

    private final ApartmentService apartmentService;
    private final ModelMapper modelMapper;

    @Autowired
    public ApartmentsController(ApartmentService apartmentService,
                                ModelMapper modelMapper) {
        this.apartmentService = apartmentService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<Set<ApartmentViewModel>> getAll(@PathVariable(value = "buildingId") String buildingId) {

        Set<ApartmentViewModel> apartments =
                this.apartmentService.getAll(buildingId).stream()
                .map(a -> this.modelMapper.map(a, ApartmentViewModel.class))
                .collect(Collectors.toSet());

        return apartments.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(apartments);
    }

    @GetMapping("/{apartmentId}")
    public ResponseEntity<ApartmentViewModel> get(@PathVariable(value = "buildingId") String buildingId,
                                                  @PathVariable(value = "apartmentId") String apartmentId) {

        return this.apartmentService.get(apartmentId)
                .filter(a -> a.getBuilding().getId().equals(buildingId))
                .map(a -> ResponseEntity.ok()
                        .body(this.modelMapper.map(a, ApartmentViewModel.class)))
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @PostMapping
    public ResponseEntity<?> add(@PathVariable(value = "buildingId") String buildingId,
                                 @Valid @RequestBody ApartmentBindingModel apartmentBindingModel,
                                 BindingResult bindingResult, UriComponentsBuilder uriComponentsBuilder) {

        if (bindingResult.hasErrors()) {
            ApartmentErrorView apartmentErrorView =
                    this.modelMapper.map(apartmentBindingModel, ApartmentErrorView.class);

            for (FieldError error : bindingResult.getFieldErrors()) {
                String key = error.getField();
                String value = error.getDefaultMessage();

                apartmentErrorView.getViolations().getErrors().putIfAbsent(key, new HashSet<>());
                apartmentErrorView.getViolations().getErrors().get(key).add(value);
            }
            return ResponseEntity.unprocessableEntity()
                    .body(apartmentErrorView);
        }

        ApartmentServiceModel apartmentServiceModel =
                this.apartmentService.add(this.modelMapper.map(apartmentBindingModel,
                        ApartmentServiceModel.class), buildingId);

        if (apartmentServiceModel.hasErrors()) {
            ApartmentErrorView apartmentErrorView =
                    this.modelMapper.map(apartmentServiceModel, ApartmentErrorView.class);
            return ResponseEntity.unprocessableEntity().body(apartmentErrorView);
        }

        return ResponseEntity.created(uriComponentsBuilder
                .path(URI_APARTMENTS + "/{apartmentId}")
                .buildAndExpand(buildingId, apartmentServiceModel.getId())
                .toUri()).build();
    }

    @PutMapping("/{apartmentId}")
    public ResponseEntity<?> edit(@PathVariable(value = "buildingId") String buildingId,
                                  @PathVariable(value = "apartmentId") String apartmentId,
                                  @Valid @RequestBody ApartmentBindingModel apartmentBindingModel,
                                  BindingResult bindingResult, UriComponentsBuilder uriComponentsBuilder) {

       if (bindingResult.hasErrors()) {
           return ResponseEntity.unprocessableEntity()
                   .body(new ErrorResponse(bindingResult.getTarget(),
                           bindingResult.getAllErrors()));
        }

       this.apartmentService
               .edit(this.modelMapper.map(apartmentBindingModel, ApartmentServiceModel.class),
                buildingId, apartmentId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .location(uriComponentsBuilder
                        .path(URI_APARTMENTS + "/{apartmentId}")
                        .buildAndExpand(buildingId, apartmentId)
                        .toUri()).build();
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAll(@PathVariable(value = "buildingId") String buildingId,
                                       UriComponentsBuilder uriComponentsBuilder) {

        this.apartmentService.deleteAll(buildingId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .location(uriComponentsBuilder
                        .path(URI_APARTMENTS)
                        .buildAndExpand(buildingId)
                        .toUri())
                .build();
    }

    @DeleteMapping("/{apartmentId}")
    public ResponseEntity<?> delete(@PathVariable(value = "buildingId") String buildingId,
                                    @PathVariable(value = "apartmentId") String apartmentId,
                                    UriComponentsBuilder uriComponentsBuilder) {

        this.apartmentService.delete(apartmentId, buildingId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .location(uriComponentsBuilder
                        .path(URI_APARTMENTS)
                        .buildAndExpand(buildingId)
                        .toUri())
                .build();
    }
}
