package com.syn.domo.repository;

import com.syn.domo.model.entity.Apartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface ApartmentRepository extends JpaRepository<Apartment, String> {

    Optional<Apartment> findByNumberAndBuilding_Id(String apartmentNumber, String buildingId);

    Set<Apartment> findAllByBuilding_IdOrderByNumber(String buildingId);

    @Query("SELECT a FROM Apartment a " +
            "WHERE a.id = :id AND a.id IN :ids")
    Optional<Apartment> getByIdIn(@Param("id") String id,
                                  @Param("ids") Set<String> ids);
}
