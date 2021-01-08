package com.syn.domo.web.controller;

import com.syn.domo.model.service.FeeServiceModel;
import com.syn.domo.model.view.FeeViewModel;
import com.syn.domo.service.FeeService;
import com.syn.domo.web.controller.namespace.FeesNamespace;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@RestController
public class FeesController implements FeesNamespace {

    private final FeeService feeService;
    private final ModelMapper modelMapper;

    @Autowired
    public FeesController(FeeService feeService,
                          ModelMapper modelMapper) {
        this.feeService = feeService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> all(@RequestParam(required = false, defaultValue = "all") String buildingId,
                                                   @RequestParam(defaultValue = "0") int page,
                                                   @RequestParam(defaultValue = "5") int size,
                                                   @RequestParam(defaultValue = "issueDate,desc") String[] sort) {
        Map<String, Object> response =
                this.feeService.getAll(buildingId, page, size, sort);

        return response.get("fees") == null
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(response);
    }

    @GetMapping("/{feeId}")
    public ResponseEntity<FeeViewModel> one(@PathVariable(value = "feeId") String feeId) {

        Optional<FeeServiceModel> fee = this.feeService.getOne(feeId);

        return fee.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(this.modelMapper.map(fee.get(), FeeViewModel.class));
    }

    @PostMapping("/{feeId}/pay")
    public ResponseEntity<?> pay(@PathVariable(value = "buildingId") String buildingId,
                                  @PathVariable(value = "feeId") String feeId,
                                  UriComponentsBuilder uriComponentsBuilder) {

        this.feeService.pay(feeId, buildingId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .location(uriComponentsBuilder
                        .path(URI_FEES + "/{feeId}")
                        .buildAndExpand(buildingId, feeId)
                        .toUri()).build();
    }

    @DeleteMapping("/{feeId}")
    public ResponseEntity<?> delete(@PathVariable(value = "feeId") String feeId,
                                    UriComponentsBuilder uriComponentsBuilder) {

        this.feeService.delete(feeId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .location(uriComponentsBuilder
                        .path(URI_FEES)
                        .build()
                        .toUri())
                .build();
    }

    @DeleteMapping
    public ResponseEntity<?> deleteAll(@RequestParam(required = false, defaultValue = "all") String buildingId,
                                       UriComponentsBuilder uriComponentsBuilder) {

        this.feeService.deleteAll(buildingId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .location(uriComponentsBuilder
                        .path(URI_FEES)
                        .build()
                        .toUri())
                .build();
    }
}
