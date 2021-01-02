package com.syn.domo.model.entity;

import javax.persistence.*;
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
    @Column(nullable = false)
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
}
