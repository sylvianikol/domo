package com.syn.domo.service.impl;

import com.syn.domo.model.entity.Building;
import com.syn.domo.model.entity.Salary;
import com.syn.domo.model.entity.Staff;
import com.syn.domo.repository.SalaryRepository;
import com.syn.domo.service.BuildingService;
import com.syn.domo.service.SalaryService;
import com.syn.domo.service.StaffService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class SalaryServiceImpl implements SalaryService {
    private static final Logger log = LoggerFactory.getLogger(SalaryServiceImpl.class);

    private final SalaryRepository salaryRepository;
    private final StaffService staffService;
    private final BuildingService buildingService;
    private final ModelMapper modelMapper;

    public SalaryServiceImpl(SalaryRepository salaryRepository,
                             StaffService staffService, BuildingService buildingService, ModelMapper modelMapper) {
        this.salaryRepository = salaryRepository;
        this.staffService = staffService;
        this.buildingService = buildingService;
        this.modelMapper = modelMapper;
    }

    @Override
    public void generateSalaries() {
        Set<Staff> staff = this.staffService.getAll().stream()
                .map(s -> this.modelMapper.map(s, Staff.class))
                .collect(Collectors.toUnmodifiableSet());

        for (Staff employee : staff) {
            Salary salary = new Salary();
            salary.setIssueDate(LocalDate.now());
            salary.setDueDate(LocalDate.now().plusMonths(1));
            salary.setDebtors(employee.getBuildings());
            salary.setStaff(employee);

            salary.setTotal(employee.getWage()
                    .multiply(BigDecimal.valueOf(employee.getBuildings().size())));

            salary.setUnpaidTotal(salary.getTotal());
            log.info("New salary generated!");
            this.salaryRepository.saveAndFlush(salary);
        }

        log.info("SALARIES GENERATED!");
    }

    @Override
    public void paySalaries() {

        Set<Salary> unpaidSalaries = this.salaryRepository.findAllByPaidFalse();

        for (Salary salary : unpaidSalaries) {

            Set<Building> buildings = salary.getDebtors();
            BigDecimal wage = salary.getStaff().getWage();

            for (Building building : buildings) {

                BigDecimal newBudget = building.getBudget().subtract(wage);

                if (newBudget.compareTo(BigDecimal.ZERO) >= 0) {
                    salary.setUnpaidTotal(salary.getUnpaidTotal().subtract(wage));
                    salary.getDebtors().remove(building);
                    this.buildingService.updateBudget(building.getId(), newBudget);
                }
            }

            if (salary.getDebtors().isEmpty()) {
                salary.setPaidDate(LocalDateTime.now());
                salary.setPaid(true);
            }

            this.salaryRepository.saveAndFlush(salary);
        }
    }
}
