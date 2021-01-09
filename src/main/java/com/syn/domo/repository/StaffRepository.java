package com.syn.domo.repository;

import com.syn.domo.model.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface StaffRepository extends JpaRepository<Staff, String> {

    Optional<Staff> findByEmail(String email);

    Optional<Staff> findByPhoneNumber(String phoneNumber);

    Set<Staff> findAllByIdIn(Set<String> ids);

    @Modifying
    @Query(value = "DELETE FROM `staff_buildings` " +
            "WHERE `staff_id` = ?1 ", nativeQuery = true)
    void cancelBuildingAssignments(String staffId);
}
