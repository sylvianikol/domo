package com.syn.domo.repository;

import com.syn.domo.model.entity.Apartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface ApartmentRepository extends JpaRepository<Apartment, String> {

    Optional<Apartment> findByNumber(String apartmentNumber);

    Set<Apartment> findAllByOrderByNumber();
}
