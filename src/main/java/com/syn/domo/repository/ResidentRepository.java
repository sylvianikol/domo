package com.syn.domo.repository;

import com.syn.domo.model.entity.Resident;
import com.syn.domo.specification.ResidentFilterSpecification;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ResidentRepository extends JpaRepository<Resident, String>, JpaSpecificationExecutor<Resident> {

    Set<Resident> findAllByIdIn(Set<String> ids);

    @Query("SELECT r FROM Resident r " +
            "JOIN r.apartments a " +
            "WHERE a.id = :apartmentId " +
            "AND r.id = :residentId ")
    Optional<Resident> getOneByIdAndApartmentId(@Param(value = "apartmentId") String apartmentId,
                                                @Param(value = "residentId") String residentId);

    @Query("SELECT r FROM Resident r " +
            "JOIN r.apartments a " +
            "WHERE r.id = :residentId " +
            "AND a.building.id = :buildingId")
    Optional<Resident> getOneByIdAndBuildingId(@Param(value = "residentId") String residentId,
                                               @Param(value = "buildingId") String buildingId);

    @Query("SELECT r FROM Resident r " +
            "JOIN r.apartments a " +
            "WHERE r.id = :id AND " +
            "a.id = :apartmentId " +
            "AND a.building.id = :buildingId ")
    Optional<Resident> getOneByIdAndBuildingIdAndApartmentId
            (@Param(value = "id") String id,
             @Param(value = "buildingId") String buildingId,
             @Param(value = "apartmentId") String apartmentId);

    @Query("SELECT r FROM Resident r " +
            "JOIN r.apartments a " +
            "WHERE a.building.id = :buildingId ")
    Set<Resident> getAllByBuildingId
            (@Param(value = "buildingId") String buildingId);

    @Query("SELECT r FROM Resident r " +
            "JOIN r.apartments a " +
            "WHERE a.id = :apartmentId ")
    Set<Resident> getAllByApartmentId(@Param(value = "apartmentId") String apartmentId);
}
