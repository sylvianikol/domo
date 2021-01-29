package com.syn.domo.service;

import com.syn.domo.model.service.SalaryServiceModel;
import com.syn.domo.web.filter.SalaryFilter;
import org.springframework.data.domain.Pageable;

import java.util.Set;

public interface SalaryService {

    void generateSalaries();

    void paySalaries();

    Set<SalaryServiceModel> getAll(SalaryFilter salaryFilter, Pageable pageable);

    int deleteAll(SalaryFilter salaryFilter);

    void delete(String salaryId);

    void pay(String salaryId);
}
