package com.syn.domo.repository;

import com.syn.domo.model.entity.Building;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface BuildingRepository extends JpaRepository<Building, String> {

    Optional<Building> findById(String id);

    Set<Building> findAllByArchivedOnNullOrderByName();

    Set<Building> findAllByArchivedOnNotNullOrderByName();

    Optional<Building> findByNameAndAddressAndNeighbourhood
            (String buildingName, String buildingAddress, String neighbourhood);

    Optional<Building> findByNameAndAddress(String buildingName, String buildingAddress);
}
