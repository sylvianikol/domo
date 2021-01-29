package com.syn.domo.service;

import com.syn.domo.model.service.SalaryServiceModel;
import com.syn.domo.web.filter.SalaryFilter;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.Set;

public interface SalaryService {

    Set<SalaryServiceModel> getAll(SalaryFilter salaryFilter, Pageable pageable);

    Optional<SalaryServiceModel> get(String salaryId);

    void pay(String salaryId);

    int deleteAll(SalaryFilter salaryFilter);

    void delete(String salaryId);

    void generateSalaries();

    void paySalaries();
}
