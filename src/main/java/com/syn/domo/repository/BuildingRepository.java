package com.syn.domo.repository;

import com.syn.domo.model.entity.Building;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface BuildingRepository extends JpaRepository<Building, String> {

    Optional<Building> findById(String id);

    Optional<Building> findByAddress(String address);

    Optional<Building> findByNameAndNeighbourhood(String buildingName, String neighbourhood);

    Optional<Building> findByNameAndAddressAndNeighbourhood
            (String buildingName, String buildingAddress, String neighbourhood);

    @Query("SELECT b FROM Building b " +
            "JOIN b.staff s " +
            "WHERE s.id = :staffId")
    Set<Building> getAllByStaffId(@Param(value = "staffId") String staffId);
}
