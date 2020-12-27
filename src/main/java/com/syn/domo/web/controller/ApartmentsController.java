package com.syn.domo.web.controller;

import com.syn.domo.model.ErrorResponse;
import com.syn.domo.model.binding.ApartmentAddBindingModel;
import com.syn.domo.model.binding.ApartmentEditBindingModel;
import com.syn.domo.model.service.ApartmentServiceModel;
import com.syn.domo.model.view.ApartmentViewModel;
import com.syn.domo.service.ApartmentService;
import com.syn.domo.service.BuildingService;
import com.syn.domo.web.controller.namespace.ApartmentsNamespace;
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
public class ApartmentsController implements ApartmentsNamespace {

    private final ApartmentService apartmentService;
    private final BuildingService buildingService;
    private final ModelMapper modelMapper;

    @Autowired
    public ApartmentsController(ApartmentService apartmentService, BuildingService buildingService, ModelMapper modelMapper) {
        this.apartmentService = apartmentService;
        this.buildingService = buildingService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<Set<ApartmentViewModel>> all(@PathVariable(value = "buildingId") String buildingId) {

        Set<ApartmentViewModel> apartments =
                this.apartmentService.getAllByBuildingId(buildingId).stream()
                .map(a -> this.modelMapper.map(a, ApartmentViewModel.class))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        return apartments.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(apartments);
    }

    @GetMapping("/{apartmentId}")
    public ResponseEntity<ApartmentViewModel> one(@PathVariable(value = "buildingId") String buildingId,
                                                  @PathVariable(value = "apartmentId") String apartmentId) {

        return this.apartmentService.getById(apartmentId)
                .filter(a -> a.getBuilding().getId().equals(buildingId))
                .map(a -> ResponseEntity.ok()
                        .body(this.modelMapper.map(a, ApartmentViewModel.class)))
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @PostMapping
    public ResponseEntity<?> add(@PathVariable(value = "buildingId") String buildingId,
                                 @Valid @RequestBody ApartmentAddBindingModel apartmentAddBindingModel,
                                 BindingResult bindingResult, UriComponentsBuilder uriComponentsBuilder) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.unprocessableEntity()
                    .body(new ErrorResponse(bindingResult.getTarget(),
                            bindingResult.getAllErrors()));
        }

        String apartmentId =
                this.apartmentService.add(this.modelMapper.map(apartmentAddBindingModel,
                        ApartmentServiceModel.class), buildingId).getId();

        return ResponseEntity.created(uriComponentsBuilder
                .path(URI_APARTMENTS + "/{apartmentId}")
                .buildAndExpand(buildingId, apartmentId)
                .toUri()).build();
    }

    @PutMapping("/{apartmentId}")
    public ResponseEntity<?> edit(@PathVariable(value = "buildingId") String buildingId,
                                  @PathVariable(value = "apartmentId") String apartmentId,
                                  @Valid @RequestBody ApartmentEditBindingModel apartmentEditBindingModel,
                                  BindingResult bindingResult, UriComponentsBuilder uriComponentsBuilder) {

       if (bindingResult.hasErrors()) {
           return ResponseEntity.unprocessableEntity()
                   .body(new ErrorResponse(bindingResult.getTarget(),
                           bindingResult.getAllErrors()));
        }

       this.apartmentService
               .edit(this.modelMapper.map(apartmentEditBindingModel,
                       ApartmentServiceModel.class),
                buildingId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .location(uriComponentsBuilder
                        .path(URI_APARTMENTS + "/{apartmentId}")
                        .buildAndExpand(buildingId, apartmentId)
                        .toUri()).build();
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
