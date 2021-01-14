package com.syn.domo.repository;

import com.syn.domo.model.entity.Child;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ChildRepository extends JpaRepository<Child, String> {

    Optional<Child> findByIdAndApartmentId(String childId, String apartmentId);

    Set<Child> findAllByApartmentId(String apartmentId);

    Optional<Child> findByFirstNameAndLastNameAndApartmentId
            (String firstName, String lastName, String apartmentId);

    @Query("SELECT c FROM Child c " +
            "JOIN c.parents p " +
            "WHERE p.id = :parentId ")
    Set<Child> getAllByParentId(@Param(value = "parentId") String parentId);

    @Query("SELECT c FROM Child c " +
            "WHERE c.apartment.building.id = :buildingId")
    Set<Child> getAllByBuildingId(@Param(value = "buildingId") String buildingId);

    @Modifying
    @Query(value = "DELETE FROM `children_parents` " +
            "WHERE `child_id` = ?1 ", nativeQuery = true)
    void severRelations(String childId);
}
