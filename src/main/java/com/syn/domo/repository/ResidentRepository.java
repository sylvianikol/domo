package com.syn.domo.repository;

import com.syn.domo.model.entity.Resident;
import com.syn.domo.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface ResidentRepository extends JpaRepository<Resident, String> {

    @Query("SELECT r FROM Resident r " +
            "JOIN r.apartments a " +
            "WHERE a.id = :apartmentId " +
            "AND a.building.id = :buildingId ")
    Set<Resident> getAllByBuildingIdAndApartmentId
            (@Param(value = "buildingId") String buildingId,
             @Param(value = "apartmentId") String apartmentId);

    @Query("SELECT r FROM Resident r " +
            "JOIN r.apartments a " +
            "WHERE r.id = :id AND " +
            "a.id = :apartmentId " +
            "AND a.building.id = :buildingId ")
    Optional<Resident> getOneByIdAndBuildingIdAndApartmentId
            (@Param(value = "id") String id,
             @Param(value = "buildingId") String buildingId,
             @Param(value = "apartmentId") String apartmentId);

    Set<Resident> findAllByIdIn(Set<String> ids);

}
