package com.syn.domo.model.entity;

import javax.persistence.*;
import java.util.Objects;
import java.util.Set;

import static javax.persistence.CascadeType.MERGE;
import static javax.persistence.CascadeType.REFRESH;
import static javax.persistence.FetchType.EAGER;

@Entity
//@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Table(name = "users")
public class UserEntity extends BaseUserEntity {

    private String email;
    private String password;
    private String phoneNumber;
    private Set<Role> roles;
    private Apartment apartment;
    Set<Child> children;

    public UserEntity() {
    }
    @Column(unique = true, nullable = false)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Column
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Column(name = "phone_number", nullable = false, length = 20)
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String personalNumber) {
        this.phoneNumber = personalNumber;
    }

    @ManyToMany(cascade = { MERGE, REFRESH }, fetch = EAGER)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    @ManyToOne
    public Apartment getApartment() {
        return apartment;
    }

    public void setApartment(Apartment apartment) {
        this.apartment = apartment;
    }

    @ManyToMany(mappedBy = "parents", fetch = FetchType.EAGER)
    public Set<Child> getChildren() {
        return children;
    }

    public void setChildren(Set<Child> children) {
        this.children = children;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserEntity)) return false;
        if (!super.equals(o)) return false;
        UserEntity that = (UserEntity) o;
        return Objects.equals(email, that.email) &&
                Objects.equals(password, that.password) &&
                Objects.equals(phoneNumber, that.phoneNumber) &&
                Objects.equals(roles, that.roles) &&
                Objects.equals(apartment, that.apartment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), email, password, phoneNumber, roles, apartment);
    }
}
