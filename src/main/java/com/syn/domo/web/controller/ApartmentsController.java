package com.syn.domo.web.controller;

import com.syn.domo.exception.*;
import com.syn.domo.model.binding.ApartmentAddBindingModel;
import com.syn.domo.model.binding.ApartmentEditBindingModel;
import com.syn.domo.model.service.ApartmentServiceModel;
import com.syn.domo.model.view.ApartmentAddViewModel;
import com.syn.domo.model.view.ApartmentEditViewModel;
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
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
public class ApartmentsController implements ApartmentsNamespace {
    private static final String MANAGE_APARTMENTS_TITLE = "Manage Apartments";
    private static final String ADD_APARTMENTS_TITLE = "Add Apartment";
    private static final String ALL_APARTMENTS = "All Apartments in ";
    private static final String APARTMENT_DETAILS = "Apartment Details";

    private final ApartmentService apartmentService;
    private final BuildingService buildingService;
    private final ModelMapper modelMapper;

    @Autowired
    public ApartmentsController(ApartmentService apartmentService, BuildingService buildingService, ModelMapper modelMapper) {
        this.apartmentService = apartmentService;
        this.buildingService = buildingService;
        this.modelMapper = modelMapper;
    }
    // how to -> /buildings/buildingId/apartments/apartmentId
    // https://www.youtube.com/watch?t=6921&v=un6U-_65zhE&feature=youtu.be

    @GetMapping
    public ResponseEntity<Set<ApartmentViewModel>> all(@PathVariable(value = "buildingId") String buildingId) {
        Set<ApartmentServiceModel> apartmentServiceModels =
                this.apartmentService.getAllApartmentsByBuildingId(buildingId);
        Set<ApartmentViewModel> apartments = apartmentServiceModels.stream()
                .map(apartmentServiceModel -> this.modelMapper.map(apartmentServiceModel, ApartmentViewModel.class))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        return apartments.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(apartments);
    }

    @GetMapping("/{apartmentId}")
    public ResponseEntity<ApartmentViewModel> one(@PathVariable(value = "buildingId") String buildingId,
                                                  @PathVariable(value = "apartmentId") String apartmentId) {
        Optional<ApartmentServiceModel> apartment =
                this.apartmentService.getById(apartmentId);

        return apartment.filter(a -> a.getBuilding().getId().equals(buildingId))
                .map(a -> ResponseEntity.ok()
                        .body(this.modelMapper.map(a, ApartmentViewModel.class)))
                .orElseGet(() -> ResponseEntity.notFound().build());

    }

    @PostMapping
    public ResponseEntity<ApartmentAddViewModel> add(@PathVariable(value = "buildingId") String buildingId,
                                                     @Valid @RequestBody ApartmentAddBindingModel apartmentAddBindingModel,
                                                     BindingResult bindingResult, UriComponentsBuilder uriComponentsBuilder) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.unprocessableEntity()
                    .body(this.modelMapper.map(bindingResult.getTarget(),
                            ApartmentAddViewModel.class));
        }

        String apartmentId;

        try {
            apartmentId =
                    this.apartmentService.add(this.modelMapper.map(apartmentAddBindingModel,
                            ApartmentServiceModel.class), buildingId).getId();
        } catch (BuildingNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (ApartmentAlreadyExistsException | FloorNotValidException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(this.modelMapper.map(bindingResult.getTarget(),
                            ApartmentAddViewModel.class));
        }

        return ResponseEntity.created(uriComponentsBuilder
                .path(URI_APARTMENTS + "/{apartmentId}")
                .buildAndExpand(buildingId, apartmentId)
                .toUri()).build();
    }

    @PutMapping("/{apartmentId}")
    public ResponseEntity<ApartmentEditViewModel> edit(@PathVariable(value = "buildingId") String buildingId,
                                                       @PathVariable(value = "apartmentId") String apartmentId,
                                                       @Valid @RequestBody ApartmentEditBindingModel apartmentEditBindingModel,
                                                       BindingResult bindingResult, UriComponentsBuilder uriComponentsBuilder) {

       if (bindingResult.hasErrors()) {
            return ResponseEntity.unprocessableEntity()
                    .body(this.modelMapper.map(apartmentEditBindingModel, ApartmentEditViewModel.class));
        }

        try {
            this.apartmentService.edit(this.modelMapper
                    .map(apartmentEditBindingModel, ApartmentServiceModel.class),
                    buildingId);

        } catch (BuildingNotFoundException | ApartmentNotFoundException ex) {
            return ResponseEntity.notFound().build();
        } catch (ApartmentAlreadyExistsException | FloorNotValidException | SameDataException ex) {
            ApartmentServiceModel apartmentServiceModel =
                    this.apartmentService.getByNumberAndBuildingId(
                            apartmentEditBindingModel.getNumber(), buildingId);
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(this.modelMapper.map(apartmentServiceModel, ApartmentEditViewModel.class));
        }

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .location(uriComponentsBuilder
                        .path(URI_APARTMENTS + "/{apartmentId}")
                        .buildAndExpand(buildingId, apartmentId)
                        .toUri()).build();
    }
}
