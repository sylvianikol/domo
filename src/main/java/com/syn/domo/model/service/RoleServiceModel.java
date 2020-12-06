package com.syn.domo.model.service;

import com.syn.domo.model.entity.UserRole;

public class RoleServiceModel {

    private UserRole roleType;

    public RoleServiceModel() {
    }

    public UserRole getRoleType() {
        return roleType;
    }

    public void setRoleType(UserRole roleType) {
        this.roleType = roleType;
    }
}
