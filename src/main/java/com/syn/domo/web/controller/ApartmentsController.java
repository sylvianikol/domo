package com.syn.domo.web.controller;

import com.syn.domo.model.binding.ApartmentBindingModel;
import com.syn.domo.model.service.ApartmentServiceModel;
import com.syn.domo.model.view.ApartmentViewModel;
import com.syn.domo.service.ApartmentService;
import com.syn.domo.model.view.ResponseModel;
import com.syn.domo.web.filter.ApartmentFilter;
import com.syn.domo.web.controller.namespace.ApartmentsNamespace;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import org.springframework.data.domain.Pageable;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.syn.domo.common.ResponseStatusMessages.DELETE_FAILED;
import static com.syn.domo.common.ResponseStatusMessages.DELETE_SUCCESSFUL;

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

    @GetMapping("/all")
    public ResponseEntity<Set<ApartmentViewModel>> getAll(@RequestParam(required = false,
                                                                name = "buildingId") String buildingId,
                                                          Pageable pageable) {

        Set<ApartmentViewModel> apartments = this.apartmentService
                .getAll(new ApartmentFilter(buildingId), pageable).stream()
                .map(a -> this.modelMapper.map(a, ApartmentViewModel.class))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        return apartments.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(apartments);
    }

    @GetMapping("/{apartmentId}")
    public ResponseEntity<ApartmentViewModel> get(@PathVariable(value = "apartmentId") String apartmentId) {

        Optional<ApartmentServiceModel> apartment = this.apartmentService.get(apartmentId);

        return apartment.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(this.modelMapper.map(apartment.get(), ApartmentViewModel.class));

    }

    @PostMapping("/add")
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
                .buildAndExpand(responseModel.getId())
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
                : ResponseEntity.status(HttpStatus.NO_CONTENT)
                .location(uriComponentsBuilder.path(URI_APARTMENTS + "/{apartmentId}")
                        .buildAndExpand(responseModel.getId())
                        .toUri()).build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteAll(@RequestParam(required = false,
                                                     name = "buildingId") String buildingId) {

        int result = this.apartmentService.deleteAll(new ApartmentFilter(buildingId));

        return result == 0
                ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(DELETE_FAILED)
                : ResponseEntity.ok().body(String.format(DELETE_SUCCESSFUL, result, "apartments"));
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
