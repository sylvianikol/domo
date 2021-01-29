package com.syn.domo.web.controller;

import com.syn.domo.model.view.SalaryViewModel;
import com.syn.domo.service.SalaryService;
import com.syn.domo.web.controller.namespace.SalariesNamespace;
import com.syn.domo.web.filter.SalaryFilter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.mail.MessagingException;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import static com.syn.domo.common.ResponseStatusMessages.DELETE_FAILED;
import static com.syn.domo.common.ResponseStatusMessages.DELETE_SUCCESSFUL;

@RestController
public class SalariesController implements SalariesNamespace {

    private final SalaryService salaryService;
    private final ModelMapper   modelMapper;

    @Autowired
    public SalariesController(SalaryService salaryService, ModelMapper modelMapper) {
        this.salaryService = salaryService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/all")
    public ResponseEntity<Set<SalaryViewModel>> getAll(@RequestParam(required = false, name = "buildingId") String buildingId,
                                                       @RequestParam(required = false, name = "staffId") String staffId,
                                                       @RequestParam(required = false, name = "isPaid") Boolean isPaid,
                                                       Pageable pageable) {
        Set<SalaryViewModel> salaries = this.salaryService
                .getAll(new SalaryFilter(buildingId, staffId, isPaid), pageable).stream()
                .map(s -> this.modelMapper.map(s, SalaryViewModel.class))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        return salaries.isEmpty()
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(salaries);
    }

    @GetMapping("/{salaryId}")
    public ResponseEntity<SalaryViewModel> get(@PathVariable(value = "salaryId") String salaryId) {
        return null;
    }

    @PostMapping("{salaryId}/pay")
    public ResponseEntity<?> pay(@PathVariable(value = "salaryId") String salaryId,
                                 UriComponentsBuilder uriComponentsBuilder) throws MessagingException, InterruptedException {

        this.salaryService.pay(salaryId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .location(uriComponentsBuilder.path(URI_SALARIES + "/{salaryId}").buildAndExpand(salaryId).toUri())
                .build();
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteAll(@RequestParam(required = false, name = "buildingId") String buildingId,
                                       @RequestParam(required = false, name = "staffId") String staffId,
                                       @RequestParam(required = false, name = "isPaid") Boolean isPaid) {

        int result = this.salaryService.deleteAll(new SalaryFilter(buildingId, staffId, isPaid));

        return result == 0
                ? ResponseEntity.status(HttpStatus.NOT_FOUND).body(DELETE_FAILED)
                : ResponseEntity.ok().body(String.format(DELETE_SUCCESSFUL, result, "salaries"));
    }

    @DeleteMapping("/{salaryId}")
    public ResponseEntity<?> delete(@PathVariable(value = "salaryId") String salaryId,
                                    UriComponentsBuilder uriComponentsBuilder) {

        this.salaryService.delete(salaryId);

        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .location(uriComponentsBuilder.path(URI_SALARIES).build().toUri())
                .build();
    }
}
