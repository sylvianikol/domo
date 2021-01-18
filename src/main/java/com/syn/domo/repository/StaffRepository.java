package com.syn.domo.repository;

import com.syn.domo.model.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface StaffRepository extends JpaRepository<Staff, String>, JpaSpecificationExecutor<Staff> {

    Set<Staff> findAllByIdIn(Set<String> ids);

    @Modifying
    @Query(value = "DELETE FROM `staff_buildings` " +
            "WHERE `staff_id` = ?1 ", nativeQuery = true)
    void cancelBuildingAssignments(String staffId);

    @Query("SELECT s FROM Staff s " +
            "JOIN s.buildings b " +
            "WHERE b.id = :buildingId ")
    Set<Staff> getAllByBuildingId(@Param(value = "buildingId") String buildingId);

}
