package com.syn.domo.repository;

import com.syn.domo.model.entity.Child;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface ChildRepository extends JpaRepository<Child, String> {

    @Query("SELECT c FROM Child c " +
            "WHERE c.apartment.id = :apartmentId " +
            "AND c.apartment.building.id = :buildingId ")
    Set<Child> getAllByApartmentIdAndBuildingId(@Param(value = "buildingId") String buildingId,
                                                @Param(value = "apartmentId") String apartmentId);

    Optional<Child> findByFirstNameAndLastNameAndApartmentId
            (String firstName, String lastName, String apartmentId);

    @Query("SELECT c FROM Child c " +
            "JOIN c.parents p " +
            "WHERE c.id = :id AND " +
            "p.id IN :ids ")
    Optional<Child> getOneByIdAndParentsIds
            (@Param(value = "id") String id,
             @Param(value = "ids") Set<String> ids);
}
