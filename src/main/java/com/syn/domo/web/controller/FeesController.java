package com.syn.domo.web.controller;

import com.syn.domo.model.service.FeeServiceModel;
import com.syn.domo.model.view.FeeViewModel;
import com.syn.domo.service.FeeService;
import com.syn.domo.web.controller.namespace.FeesNamespace;
import com.syn.domo.web.filter.FeeFilter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.mail.MessagingException;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.data.domain.Pageable;

import static com.syn.domo.common.ResponseStatusMessages.DELETE_FAILED;
import static com.syn.domo.common.ResponseStatusMessages.DELETE_SUCCESSFUL;

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

    @GetMapping("/all")
    public ResponseEntity<Set<FeeViewModel>> getAll(@RequestParam(required = false, name = "buildingId") String buildingId,
                                                    @RequestParam(required = false, name = "apartmentId") String apartmentId,
                                                    Pageable pageable) {

        Set<FeeViewModel> fees = this.feeService
                .getAll(new FeeFilter(buildingId, apartmentId), pageable).stream()
                .map(f -> this.modelMapper.map(f, FeeViewModel.class))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        return fees.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(fees);

    }

    @GetMapping("/{feeId}")
    public ResponseEntity<FeeViewModel> get(@PathVariable(value = "feeId") String feeId) {

        Optional<FeeServiceModel> fee = this.feeService.get(feeId);

        return fee.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(this.modelMapper.map(fee.get(), FeeViewModel.class));
    }

    @PostMapping("{feeId}/pay")
    public ResponseEntity<?> pay(@PathVariable(value = "feeId") String feeId,
                                 @RequestParam(name = "userId") String userId,
                                 UriComponentsBuilder uriComponentsBuilder) throws MessagingException {

       this.feeService.pay(userId, feeId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .location(uriComponentsBuilder.path(URI_FEES + "/{feeId}").buildAndExpand(feeId).toUri())
                .build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteAll(@RequestParam(required = false) String buildingId,
                                       @RequestParam(required = false) String apartmentId) {

        int result = this.feeService.deleteAll(new FeeFilter(buildingId, apartmentId));

        return result == 0
                ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(DELETE_FAILED)
                : ResponseEntity.ok().body(String.format(DELETE_SUCCESSFUL, result, "fees"));
    }

    @DeleteMapping("/{feeId}")
    public ResponseEntity<?> delete(@PathVariable(value = "feeId") String feeId,
                                    UriComponentsBuilder uriComponentsBuilder) {

        this.feeService.delete(feeId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .location(uriComponentsBuilder.path(URI_FEES).build().toUri())
                .build();
    }


}
