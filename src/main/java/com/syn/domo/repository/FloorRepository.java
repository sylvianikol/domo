package com.syn.domo.repository;

import com.syn.domo.model.entity.Floor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface FloorRepository extends JpaRepository<Floor, String> {

    Optional<Floor> findByNumberAndBuilding_Id(int number, String buildingId);

    List<Floor> findAllByOrderByNumber();

    Set<Floor> findAllByBuilding_IdOrderByNumber(String buildingId);
}
