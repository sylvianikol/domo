package com.syn.domo.repository;

import com.syn.domo.model.entity.Floor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FloorRepository extends JpaRepository<Floor, String> {
    Optional<Floor> findByNumber(Integer number);

    @Query("SELECT sum(f.apartmentsPerFloor) FROM Floor f ")
    int sumTotalApartments();
}
