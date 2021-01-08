package com.syn.domo.repository;

import com.syn.domo.model.entity.Fee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface FeeRepository extends JpaRepository<Fee, String> {

    @Query("SELECT f FROM Fee f " +
            "WHERE f.apartment.building.id = :buildingId ")
    Page<Fee> getAllByBuildingIdWithPagingSort(@Param(value = "buildingId") String buildingId,
                                               Pageable pagingSort);

    @Query("SELECT f FROM Fee f " +
            "WHERE f.apartment.building.id = :buildingId ")
    Set<Fee> getAllByBuildingId(@Param(value = "buildingId") String buildingId);

    @Query("SELECT f FROM Fee f " +
            "WHERE f.id = :id " +
            "AND f.apartment.building.id = :buildingId ")
    Optional<Fee> getOneByIdAndBuildingId(@Param(value = "id") String id,
                                          @Param(value = "buildingId") String buildingId);
}
