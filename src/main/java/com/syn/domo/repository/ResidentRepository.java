package com.syn.domo.repository;

import com.syn.domo.model.entity.Resident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface ResidentRepository extends JpaRepository<Resident, String>, JpaSpecificationExecutor<Resident> {

    Set<Resident> findAllByIdIn(Set<String> ids);

   // DELETE
//    @Query("SELECT r FROM Resident r " +
//            "JOIN r.apartments a " +
//            "WHERE r.id = :residentId " +
//            "AND a.building.id = :buildingId")
//    Optional<Resident> getOneByIdAndBuildingId(@Param(value = "residentId") String residentId,
//                                               @Param(value = "buildingId") String buildingId);
//
}
