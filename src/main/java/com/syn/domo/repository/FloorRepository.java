package com.syn.domo.repository;

import com.syn.domo.model.entity.Floor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface FloorRepository extends JpaRepository<Floor, String> {

    Floor findByNumber(int number);

    @Query("SELECT sum(f.capacity) FROM Floor f ")
    int sumTotalApartments();

    List<Floor> findAllByOrderByNumber();

    Set<Floor> findAllByBuilding_Id(Long id);
}
