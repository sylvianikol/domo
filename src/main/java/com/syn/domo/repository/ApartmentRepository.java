package com.syn.domo.repository;

import com.syn.domo.model.entity.Apartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface ApartmentRepository extends JpaRepository<Apartment, String> {

    Optional<Apartment> findByNumberAndBuilding_Id(String apartmentNumber, String buildingId);

    Optional<Apartment> findByIdAndBuildingId(String apartmentId, String buildingId);

    Set<Apartment> findAllByBuildingIdOrderByNumber(String id);

}
