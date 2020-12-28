package com.syn.domo.repository;

import com.syn.domo.model.entity.Child;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface ChildRepository extends JpaRepository<Child, String> {

    Set<Child> findAllByApartment_Id(String apartmentId);

    Optional<Child> findByFirstNameAndLastNameAndApartment_Id
            (String firstName, String lastName, String apartmentId);
}
