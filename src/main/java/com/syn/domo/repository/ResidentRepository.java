package com.syn.domo.repository;

import com.syn.domo.model.entity.Resident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface ResidentRepository extends JpaRepository<Resident, String> {

    @Query("SELECT r FROM Resident r " +
            "WHERE r.apartment.id = :apartmentId " +
            "AND r.apartment.building.id = :buildingId ")
    Set<Resident> getAllByApartmentIdAndBuildingId
            (@Param(value = "buildingId") String buildingId,
             @Param(value = "apartmentId") String apartmentId);

    Set<Resident> findAllByIdIn(Set<String> ids);

    Optional<Resident> findByEmail(String email);

    Optional<Resident> findByFirstNameAndLastName(String firstName, String lastName);
}
