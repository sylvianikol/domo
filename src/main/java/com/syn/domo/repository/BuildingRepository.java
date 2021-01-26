package com.syn.domo.repository;

import com.syn.domo.model.entity.Building;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface BuildingRepository extends JpaRepository<Building, String> {

    Optional<Building> findByAddress(String address);

    Optional<Building> findByIdIsNotAndAddress(String buildingId, String address);

    Optional<Building> findByNameAndNeighbourhood(String buildingName, String neighbourhood);

    Set<Building> findAllByIdIn(Set<String> ids);
}
