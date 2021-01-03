package com.syn.domo.model.entity;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

import static javax.persistence.FetchType.*;

@Entity
@Table(name = "roles")
public class Role extends BaseEntity {

    private UserRole name;
    private Set<UserEntity> users;

    public Role() {
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    public UserRole getName() {
        return name;
    }

    public void setName(UserRole name) {
        this.name = name;
    }

    @ManyToMany(mappedBy = "roles", fetch = EAGER)
    public Set<UserEntity> getUsers() {
        return users;
    }

    public void setUsers(Set<UserEntity> users) {
        this.users = users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Role)) return false;
        if (!super.equals(o)) return false;
        Role role = (Role) o;
        return name == role.name;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name);
    }
}
