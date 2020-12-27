package com.syn.domo.repository;

import com.syn.domo.model.entity.Resident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface ResidentRepository extends JpaRepository<Resident, String> {

    Set<Resident> findAllByApartment_Id(String apartmentId);

    Optional<Resident> findByEmail(String email);
}
