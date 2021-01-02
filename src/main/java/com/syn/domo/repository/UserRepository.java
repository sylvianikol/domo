package com.syn.domo.repository;

import com.syn.domo.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {

    @Query("SELECT u FROM UserEntity u " +
            "WHERE u.apartment.id = :apartmentId " +
            "AND u.apartment.building.id = :buildingId ")
    Set<UserEntity> getAllByApartmentIdAndBuildingId
            (@Param(value = "buildingId") String buildingId,
             @Param(value = "apartmentId") String apartmentId);

    Set<UserEntity> findAllByIdIn(Set<String> ids);

    Optional<UserEntity> findByEmail(String email);

}
