package com.syn.domo.web.controller;

import com.syn.domo.model.view.ResponseModel;
import com.syn.domo.model.binding.ResidentBindingModel;
import com.syn.domo.model.service.ResidentServiceModel;
import com.syn.domo.model.view.ResidentViewModel;
import com.syn.domo.service.ResidentService;
import com.syn.domo.web.controller.namespace.ResidentsNamespace;
import com.syn.domo.web.filter.ResidentFilter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.mail.MessagingException;
import javax.validation.Valid;
import org.springframework.data.domain.Pageable;

import java.net.URI;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static com.syn.domo.common.ResponseStatusMessages.DELETE_FAILED;
import static com.syn.domo.common.ResponseStatusMessages.DELETE_SUCCESSFUL;

@RestController
public class ResidentsController implements ResidentsNamespace {

    private final ResidentService residentService;
    private final ModelMapper modelMapper;

    @Autowired
    public ResidentsController(ResidentService residentService, ModelMapper modelMapper) {
        this.residentService = residentService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/all")
    public ResponseEntity<Set<ResidentViewModel>> getAll(@RequestParam(required = false,
                                                                 name = "buildingId") String buildingId,
                                                         @RequestParam(required = false,
                                                                 name = "apartmentId") String apartmentId,
                                                         Pageable pageable) {
        Set<ResidentViewModel> residents =
                this.residentService.getAll(new ResidentFilter(buildingId, apartmentId), pageable).stream()
                .map(r -> this.modelMapper.map(r, ResidentViewModel.class))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        return residents.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(residents);
    }

    @GetMapping("/{residentId}")
    public ResponseEntity<ResidentViewModel> get(@PathVariable(value = "residentId") String residentId) {

        Optional<ResidentServiceModel> resident =
                this.residentService.get(residentId);

        return resident.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(this.modelMapper.map(resident.get(), ResidentViewModel.class));
    }

    @PostMapping("/add")
    public ResponseEntity<?> add(@RequestParam(value = "buildingId") String buildingId,
                                 @RequestParam(value = "apartmentId") String apartmentId,
                                 @Valid @RequestBody ResidentBindingModel residentBindingModel,
                                 BindingResult bindingResult,
                                 UriComponentsBuilder uriComponentsBuilder) throws MessagingException, InterruptedException {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.unprocessableEntity()
                    .body(new ResponseModel<>(residentBindingModel, bindingResult));
        }

        ResponseModel<ResidentServiceModel> responseModel = this.residentService
                .add(this.modelMapper.map(residentBindingModel, ResidentServiceModel.class),
                buildingId, apartmentId);

        return responseModel.hasErrors()
                ? ResponseEntity.unprocessableEntity().body(responseModel)
                : ResponseEntity.created(getLocation(uriComponentsBuilder, responseModel.getId())).build();
    }

    @PutMapping("/{residentId}")
    public ResponseEntity<?> edit(@PathVariable(value = "residentId") String residentId,
                                  @Valid @RequestBody ResidentBindingModel residentBindingModel,
                                  BindingResult bindingResult, UriComponentsBuilder uriComponentsBuilder) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.unprocessableEntity()
                    .body(new ResponseModel<>(residentBindingModel, bindingResult));
        }

        ResponseModel<ResidentServiceModel> responseModel = this.residentService
                .edit(this.modelMapper.map(residentBindingModel, ResidentServiceModel.class), residentId);

        return responseModel.hasErrors()
                ? ResponseEntity.unprocessableEntity().body(responseModel)
                : ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .location(getLocation(uriComponentsBuilder, residentId))
                .build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteAll(@RequestParam(required = false, name = "buildingId") String buildingId,
                                       @RequestParam(required = false, name = "apartmentId") String apartmentId) {

        int result = this.residentService.deleteAll(new ResidentFilter(buildingId, apartmentId));

        return result == 0
                ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(DELETE_FAILED)
                : ResponseEntity.ok().body(String.format(DELETE_SUCCESSFUL, result, "residents"));

    }

    @DeleteMapping("/{residentId}")
    public ResponseEntity<?> delete(@PathVariable(value = "residentId") String residentId,
                                    UriComponentsBuilder uriComponentsBuilder) {

        this.residentService.delete(residentId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .location(uriComponentsBuilder.path(URI_RESIDENTS).build().toUri())
                .build();
    }

    private URI getLocation(UriComponentsBuilder uriComponentsBuilder, String id) {
        return uriComponentsBuilder.path(URI_RESIDENTS + "/{residentId}").buildAndExpand(id).toUri();
    }
}
