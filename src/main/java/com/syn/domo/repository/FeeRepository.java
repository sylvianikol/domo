package com.syn.domo.repository;

import com.syn.domo.model.entity.Fee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface FeeRepository extends JpaRepository<Fee, String> {

    Fee findByStartDateAndApartment_Number(LocalDate date, String number);
}
