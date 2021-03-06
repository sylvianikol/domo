package com.syn.domo.repository;

import com.syn.domo.model.entity.Fee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface FeeRepository extends JpaRepository<Fee, String>,
        JpaSpecificationExecutor<Fee> {
}
