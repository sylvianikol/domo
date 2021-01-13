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

import static com.syn.domo.common.DefaultParamValues.*;

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
    public ResponseEntity<Map<String, Object>> getAll(@RequestParam(required = false,
                                                                 defaultValue = EMPTY_URL) String buildingId,
                                                      @RequestParam(defaultValue = DEFAULT_PAGE_NUMBER) int page,
                                                      @RequestParam(defaultValue = DEFAULT_FEE_PAGE_SIZE) int size,
                                                      @RequestParam(defaultValue = DEFAULT_FEE_PAGE_SORT) String[] sort) {
        Map<String, Object> response =
                this.feeService.getAll(buildingId, page, size, sort);

        return response.get(FEES_RESPONSE_KEY) == null
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(response);
    }

    @GetMapping("/{feeId}")
    public ResponseEntity<FeeViewModel> get(@PathVariable(value = "feeId") String feeId) {

        Optional<FeeServiceModel> fee = this.feeService.get(feeId);

        return fee.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(this.modelMapper.map(fee.get(), FeeViewModel.class));
    }

    @PostMapping("/{feeId}/pay")
    public ResponseEntity<?> pay(@PathVariable(value = "feeId") String feeId,
                                  UriComponentsBuilder uriComponentsBuilder) {

        this.feeService.pay(feeId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .location(uriComponentsBuilder
                        .path(URI_FEES + "/{feeId}")
                        .buildAndExpand(feeId)
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
    public ResponseEntity<?> deleteAll(@RequestParam(required = false, defaultValue = EMPTY_URL) String buildingId,
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
