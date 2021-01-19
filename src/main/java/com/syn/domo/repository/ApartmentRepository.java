package com.syn.domo.repository;

import com.syn.domo.model.entity.Apartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface ApartmentRepository extends JpaRepository<Apartment, String>, JpaSpecificationExecutor<Apartment> {

    Optional<Apartment> findByNumberAndBuildingId(String apartmentNumber, String buildingId);

    @Query("SELECT a FROM Apartment a " +
            "WHERE a.number = :apartmentNumber " +
            "AND a.building.id = :buildingId " +
            "AND a.id NOT like :apartmentId ")
    Optional<Apartment> getDuplicateApartment(@Param(value = "apartmentNumber") String apartmentNumber,
                                              @Param(value = "buildingId") String buildingId,
                                              @Param(value = "apartmentId") String apartmentId);

    Set<Apartment> findAllByBuildingId(String buildingId);

    Optional<Apartment> findByIdAndBuildingId(String apartmentId, String buildingId);
}
