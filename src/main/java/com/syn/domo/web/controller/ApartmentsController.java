package com.syn.domo.web.controller;

import com.syn.domo.model.view.error.ErrorView;
import com.syn.domo.model.binding.ApartmentBindingModel;
import com.syn.domo.model.service.ApartmentServiceModel;
import com.syn.domo.model.view.ApartmentViewModel;
import com.syn.domo.service.ApartmentService;
import com.syn.domo.model.view.ResponseModel;
import com.syn.domo.web.controller.namespace.ApartmentsNamespace;
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

import static com.syn.domo.common.DefaultParamValues.DEFAULT_ALL;

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
    public ResponseEntity<Set<ApartmentViewModel>> getAll(@RequestParam(required = false, defaultValue = DEFAULT_ALL,
                                                                        name = "buildingId") String buildingId) {

        Set<ApartmentViewModel> apartments =
                this.apartmentService.getAll(buildingId).stream()
                .map(a -> this.modelMapper.map(a, ApartmentViewModel.class))
                .collect(Collectors.toUnmodifiableSet());

        return apartments.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(apartments);
    }

    @GetMapping("/{apartmentId}")
    public ResponseEntity<ApartmentViewModel> get(@PathVariable(value = "apartmentId") String apartmentId) {

        Optional<ApartmentServiceModel> apartment = this.apartmentService.get(apartmentId);

        return apartment.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(this.modelMapper.map(apartment, ApartmentViewModel.class));

    }

    @PostMapping
    public ResponseEntity<?> add(@RequestParam(name = "buildingId") String buildingId,
                                 @Valid @RequestBody ApartmentBindingModel apartmentBindingModel,
                                 BindingResult bindingResult, UriComponentsBuilder uriComponentsBuilder) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.unprocessableEntity()
                    .body(new ResponseModel<>(apartmentBindingModel, bindingResult));
        }

        ResponseModel<ApartmentServiceModel> responseModel =
                this.apartmentService.add(this.modelMapper.map(apartmentBindingModel,
                ApartmentServiceModel.class), buildingId);

        return  responseModel.hasErrors()
                ? ResponseEntity.unprocessableEntity().body(responseModel)
                : ResponseEntity.created(uriComponentsBuilder.path(URI_APARTMENTS + "/{apartmentId}")
                .buildAndExpand(buildingId, responseModel.getId())
                .toUri()).build();
    }

    @PutMapping("/{apartmentId}")
    public ResponseEntity<?> edit(@PathVariable(value = "apartmentId") String apartmentId,
                                  @Valid @RequestBody ApartmentBindingModel apartmentBindingModel,
                                  BindingResult bindingResult, UriComponentsBuilder uriComponentsBuilder) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.unprocessableEntity()
                    .body(new ResponseModel<>(apartmentBindingModel, bindingResult));
        }

        ResponseModel<ApartmentServiceModel> responseModel = this.apartmentService
               .edit(this.modelMapper.map(apartmentBindingModel, ApartmentServiceModel.class),
                       apartmentId);

        return responseModel.hasErrors()
                ? ResponseEntity.unprocessableEntity().body(responseModel)
                : ResponseEntity.created(uriComponentsBuilder.path(URI_APARTMENTS + "/{apartmentId}")
                .buildAndExpand(apartmentId)
                .toUri()).build();
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAll(@RequestParam(required = false, defaultValue = DEFAULT_ALL,
                                                     name = "buildingId") String buildingId,
                                       UriComponentsBuilder uriComponentsBuilder) {

        this.apartmentService.deleteAll(buildingId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .location(uriComponentsBuilder.path(URI_APARTMENTS).build().toUri())
                .build();
    }

    @DeleteMapping("/{apartmentId}")
    public ResponseEntity<?> delete(@PathVariable(value = "apartmentId") String apartmentId,
                                    UriComponentsBuilder uriComponentsBuilder) {

        this.apartmentService.delete(apartmentId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .location(uriComponentsBuilder.path(URI_APARTMENTS).build().toUri())
                .build();
    }
}
