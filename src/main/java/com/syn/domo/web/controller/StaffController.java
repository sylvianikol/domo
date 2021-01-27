package com.syn.domo.web.controller;

import com.syn.domo.model.binding.*;
import com.syn.domo.model.service.StaffServiceModel;
import com.syn.domo.model.view.StaffViewModel;
import com.syn.domo.service.StaffService;
import com.syn.domo.web.controller.namespace.StaffNamespace;
import com.syn.domo.web.filter.StaffFilter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.data.domain.Pageable;

import static com.syn.domo.common.ResponseStatusMessages.DELETE_FAILED;
import static com.syn.domo.common.ResponseStatusMessages.DELETE_SUCCESSFUL;

@RestController
public class StaffController implements StaffNamespace {

    private final StaffService staffService;
    private final ModelMapper modelMapper;

    @Autowired
    public StaffController(StaffService staffService, ModelMapper modelMapper) {
        this.staffService = staffService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/all")
    public ResponseEntity<Set<StaffViewModel>> getAll(@RequestParam(required = false,
                                                        name = "buildingId") String buildingId,
                                                      Pageable pageable) {

        Set<StaffViewModel> staff = this.staffService
                .getAll(new StaffFilter(buildingId), pageable).stream()
                .map(s -> this.modelMapper.map(s, StaffViewModel.class))
                .collect(Collectors.toCollection(LinkedHashSet::new));

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

    @PostMapping("/add")
    public ResponseEntity<?> add(@Valid @RequestBody StaffBindingModel staffBindingModel,
                                 BindingResult bindingResult,
                                 UriComponentsBuilder uriComponentsBuilder) throws MessagingException, InterruptedException {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.unprocessableEntity().body(staffBindingModel);
        }

        StaffServiceModel staffServiceModel =
                this.staffService.add(this.modelMapper.map(staffBindingModel, StaffServiceModel.class));

        return ResponseEntity.created(uriComponentsBuilder.path(URI_STAFF + "/{staffId}")
                .buildAndExpand(staffServiceModel.getId()).toUri())
                .build();
    }

    @PutMapping("/{staffId}")
    public ResponseEntity<?> edit(@PathVariable(value = "staffId") String staffId,
                                  @Valid @RequestBody StaffBindingModel staffBindingModel,
                                  BindingResult bindingResult,
                                  UriComponentsBuilder uriComponentsBuilder) {

        if (bindingResult.hasErrors()) {
            return ResponseEntity.unprocessableEntity().body(staffBindingModel);
        }

        StaffServiceModel staffServiceModel = this.staffService
                .edit(this.modelMapper.map(staffBindingModel, StaffServiceModel.class), staffId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .location(uriComponentsBuilder.path(URI_STAFF + "/{staffId}")
                    .buildAndExpand(staffId).toUri())
                .build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteAll(@RequestParam(required = false,
                                                     name = "buildingId") String buildingId) {

        int result = this.staffService.deleteAll(new StaffFilter(buildingId));

        return result == 0
                ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(DELETE_FAILED)
                : ResponseEntity.ok().body(String.format(DELETE_SUCCESSFUL, result, "staff"));
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
