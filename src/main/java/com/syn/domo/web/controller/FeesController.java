package com.syn.domo.web.controller;

import com.syn.domo.model.entity.Fee;
import com.syn.domo.model.service.FeeServiceModel;
import com.syn.domo.model.view.FeeViewModel;
import com.syn.domo.repository.FeeRepository;
import com.syn.domo.service.FeeService;
import com.syn.domo.web.controller.namespace.FeesNamespace;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import java.util.*;

@RestController
public class FeesController implements FeesNamespace {

    private final FeeRepository feeRepository;
    private final FeeService feeService;
    private final ModelMapper modelMapper;

    @Autowired
    public FeesController(FeeRepository feeRepository, FeeService feeService, ModelMapper modelMapper) {
        this.feeRepository = feeRepository;
        this.feeService = feeService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> all(@PathVariable(value = "buildingId") String buildingId,
                                                   @RequestParam(defaultValue = "1") int page,
                                                   @RequestParam(defaultValue = "5") int size,
                                                   @RequestParam(defaultValue = "issueDate,desc") String[] sort) {

        Map<String, Object> response =
                this.feeService.getAll(buildingId, page, size, sort);

        return response.get("fees") == null
                ? ResponseEntity.noContent().build()
                : ResponseEntity.ok(response);
    }

    @GetMapping("/{feeId}")
    public ResponseEntity<FeeViewModel> one(@PathVariable(value = "buildingId") String buildingId,
                                            @PathVariable(value = "feeId") String feeId) {

        Optional<FeeServiceModel> fee = this.feeService.getOne(feeId, buildingId);

        return fee.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(this.modelMapper.map(fee.get(), FeeViewModel.class));
    }
}
