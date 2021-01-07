package com.syn.domo.repository;

import com.syn.domo.model.entity.Fee;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeeRepository extends JpaRepository<Fee, String> {

    List<Fee> findAll(Sort sort);
}
