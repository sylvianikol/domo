package com.syn.domo.service.impl;

import com.syn.domo.model.entity.Role;
import com.syn.domo.model.entity.UserRole;
import com.syn.domo.repository.RoleRepository;
import com.syn.domo.service.RoleService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }


    @Override
    public void initRoles() {
        if (this.roleRepository.count() == 0) {
            for (UserRole value : UserRole.values()) {
                Role role = new Role();
                role.setName(value);
                this.roleRepository.saveAndFlush(role);
            }
        }
    }

    @Override
    public Set<Role> getAll() {
        return this.roleRepository
                .findAll().stream()
                .collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public void saveAll(Set<Role> roles) {
        this.roleRepository.saveAll(roles);
    }
}
