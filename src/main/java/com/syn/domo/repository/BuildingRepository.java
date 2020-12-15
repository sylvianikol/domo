package com.syn.domo.repository;

import com.syn.domo.model.entity.Building;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface BuildingRepository extends JpaRepository<Building, String> {

    Set<Building> findAllByOrderByName();
}
