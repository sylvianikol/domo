package com.syn.domo.repository;

import com.syn.domo.model.entity.Salary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface SalaryRepository extends JpaRepository<Salary, String>,
        JpaSpecificationExecutor<Salary> {

    Set<Salary> findAllByPaidFalse();
}
