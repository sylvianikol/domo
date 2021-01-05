package com.syn.domo.repository;

import com.syn.domo.model.entity.Staff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StaffRepository extends JpaRepository<Staff, String> {

    Optional<Staff> findByEmail(String email);

    Optional<Staff> findByPhoneNumber(String phoneNumber);
}
