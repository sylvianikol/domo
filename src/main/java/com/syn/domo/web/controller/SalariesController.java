package com.syn.domo.web.controller;

import com.syn.domo.model.view.SalaryViewModel;
import com.syn.domo.service.SalaryService;
import com.syn.domo.web.controller.namespace.SalariesNamespace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.mail.MessagingException;
import java.util.Set;

@RestController
public class SalariesController implements SalariesNamespace {

    private final SalaryService salaryService;

    @Autowired
    public SalariesController(SalaryService salaryService) {
        this.salaryService = salaryService;
    }

    @GetMapping("/all")
    public ResponseEntity<Set<SalaryViewModel>> getAll() {
        return null;
    }

    @GetMapping("/{salaryId}")
    public ResponseEntity<SalaryViewModel> get(@PathVariable(value = "salaryId") String salaryId) {
        return null;
    }

    @PostMapping("{salaryId}/pay")
    public ResponseEntity<?> pay(@PathVariable(value = "salaryId") String salaryId,
                                 UriComponentsBuilder uriComponentsBuilder) throws MessagingException, InterruptedException {

        return null;
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteAll() {
        return null;
    }

    @DeleteMapping("/{salaryId}")
    public ResponseEntity<?> delete(@PathVariable(value = "salaryId") String salaryId,
                                    UriComponentsBuilder uriComponentsBuilder) {

        return null;
    }
}
