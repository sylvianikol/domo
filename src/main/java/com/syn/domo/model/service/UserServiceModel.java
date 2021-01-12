package com.syn.domo.model.service;

import javax.validation.constraints.*;
import java.util.Objects;
import java.util.Set;

import static com.syn.domo.common.RegexPatterns.PHONE_REGEX;
import static com.syn.domo.common.ValidationErrorMessages.*;

public class UserServiceModel extends BaseUserServiceModel {

    private String email;
    private String phoneNumber;
    private boolean isActive;
    private Set<RoleServiceModel> roles;

    public UserServiceModel() {
    }

    @NotNull(message = EMAIL_NULL)
    @NotEmpty(message = EMAIL_EMPTY)
    @Email(message = EMAIL_INVALID)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @NotNull(message = PHONE_NULL)
    @NotEmpty(message = PHONE_EMPTY)
    @Pattern(regexp = PHONE_REGEX, message = PHONE_INVALID)
    @Size(max = 20, message = PHONE_LENGTH)
    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @NotNull(message = ACTIVE_STATUS_NULL)
    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Set<RoleServiceModel> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleServiceModel> roles) {
        this.roles = roles;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserServiceModel)) return false;
        if (!super.equals(o)) return false;
        UserServiceModel that = (UserServiceModel) o;
        return isActive == that.isActive &&
                Objects.equals(email, that.email) &&
                Objects.equals(phoneNumber, that.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), email, phoneNumber, isActive);
    }
}
