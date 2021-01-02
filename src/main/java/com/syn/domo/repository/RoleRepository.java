package com.syn.domo.repository;

import com.syn.domo.model.entity.Role;
import com.syn.domo.model.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {

    Optional<Role> findByName(UserRole name);
}
