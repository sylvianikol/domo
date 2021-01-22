package com.syn.domo.repository;

import com.syn.domo.model.entity.Child;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ChildRepository extends JpaRepository<Child, String>, JpaSpecificationExecutor<Child> {

    Optional<Child> findByFirstNameAndLastNameAndApartmentId
            (String firstName, String lastName, String apartmentId);

    @Modifying
    @Query(value = "DELETE FROM `children_parents` " +
            "WHERE `child_id` = ?1 ", nativeQuery = true)
    int severParentRelations(String childId);
}
