package com.syn.domo.service;

import com.syn.domo.model.entity.Role;

import java.util.Set;

public interface RoleService {

    void initRoles();

    Set<Role> getAll();

    void saveAll(Set<Role> roles);
}
