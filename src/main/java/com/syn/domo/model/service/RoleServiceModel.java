package com.syn.domo.model.service;

import com.syn.domo.model.entity.UserRole;

import java.util.Objects;
import java.util.Set;

public class RoleServiceModel extends BaseServiceModel {

    private UserRole name;
    private Set<UserServiceModel> users;

    public RoleServiceModel() {
    }

    public UserRole getName() {
        return name;
    }

    public void setName(UserRole name) {
        this.name = name;
    }

    public Set<UserServiceModel> getUsers() {
        return users;
    }

    public void setUsers(Set<UserServiceModel> users) {
        this.users = users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof RoleServiceModel)) return false;
        RoleServiceModel that = (RoleServiceModel) o;
        return name == that.name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
