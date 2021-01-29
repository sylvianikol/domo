package com.syn.domo.service.impl;

import com.syn.domo.exception.DomoEntityNotFoundException;
import com.syn.domo.exception.UnprocessableEntityException;
import com.syn.domo.model.entity.Building;
import com.syn.domo.model.entity.Salary;
import com.syn.domo.model.entity.Staff;
import com.syn.domo.model.service.SalaryServiceModel;
import com.syn.domo.repository.SalaryRepository;
import com.syn.domo.service.BuildingService;
import com.syn.domo.service.SalaryService;
import com.syn.domo.service.StaffService;
import com.syn.domo.web.filter.SalaryFilter;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.syn.domo.common.ExceptionErrorMessages.*;

@Service
public class SalaryServiceImpl implements SalaryService {
    private static final Logger log = LoggerFactory.getLogger(SalaryServiceImpl.class);

    private final SalaryRepository salaryRepository;
    private final StaffService     staffService;
    private final BuildingService  buildingService;
    private final ModelMapper      modelMapper;

    public SalaryServiceImpl(SalaryRepository salaryRepository,
                             StaffService staffService, BuildingService buildingService, ModelMapper modelMapper) {
        this.salaryRepository = salaryRepository;
        this.staffService = staffService;
        this.buildingService = buildingService;
        this.modelMapper = modelMapper;
    }

    @Override
    public Set<SalaryServiceModel> getAll(SalaryFilter salaryFilter, Pageable pageable) {

        Set<SalaryServiceModel> salaries = this.salaryRepository.findAll(salaryFilter, pageable).getContent().stream()
                .map(s -> this.modelMapper.map(s, SalaryServiceModel.class))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        return Collections.unmodifiableSet(salaries);
    }

    @Override
    public Optional<SalaryServiceModel> get(String salaryId) {

        Optional<Salary> salary = this.salaryRepository.findById(salaryId);

        return salary.isEmpty()
                ? Optional.empty()
                : Optional.of(this.modelMapper.map(salary.get(), SalaryServiceModel.class));
    }

    @Override
    public void pay(String salaryId) {

        Salary salary = this.salaryRepository.findById(salaryId)
                .orElseThrow(() -> { throw new DomoEntityNotFoundException(SALARY_NOT_FOUND); });

        if (salary.isPaid()) {
            throw new UnprocessableEntityException(SALARY_ALREADY_PAID);
        }

        Set<Building> debtors = salary.getBuildings();
        Set<Building> payers = new HashSet<>();

        BigDecimal wage = salary.getStaff().getWage();

        for (Building building : debtors) {

            BigDecimal newBudget = building.getBudget().subtract(wage);

            if (newBudget.compareTo(BigDecimal.ZERO) >= 0) {
                salary.setUnpaidTotal(salary.getUnpaidTotal().subtract(wage));
                payers.add(building);
                this.buildingService.updateBudget(building.getId(), newBudget);
            }
        }

        debtors.removeAll(payers);

        if (debtors.isEmpty()) {
            salary.setPaidDate(LocalDateTime.now());
            salary.setPaid(true);
        }

        //TODO: send notification
        this.salaryRepository.saveAndFlush(salary);
    }

    @Override
    public int deleteAll(SalaryFilter salaryFilter) {

        List<Salary> salaries = this.salaryRepository.findAll(salaryFilter);

        this.salaryRepository.deleteAll(salaries);

        return salaries.size();
    }

    @Override
    public void delete(String salaryId) {

        Salary salary = this.salaryRepository.findById(salaryId)
                .orElseThrow(() -> { throw new DomoEntityNotFoundException(SALARY_NOT_FOUND); });

        this.salaryRepository.delete(salary);
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
            salary.setBuildings(employee.getBuildings());
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
           this.pay(salary.getId());
        }
    }

}
