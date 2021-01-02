package com.syn.domo.service;

import com.syn.domo.model.entity.Role;
import com.syn.domo.model.entity.UserRole;
import com.syn.domo.model.service.RoleServiceModel;

import java.util.Optional;
import java.util.Set;

public interface RoleService {

    void initRoles();

    Set<Role> getAll();

    Optional<RoleServiceModel> getByName(UserRole name);
}
