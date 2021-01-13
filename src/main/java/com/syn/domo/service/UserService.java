package com.syn.domo.service;

import com.syn.domo.model.service.UserServiceModel;

import java.util.Optional;

public interface UserService {

    void initAdmin();

    Optional<UserServiceModel> getByEmail(String email);

    Optional<UserServiceModel> getByPhoneNumber(String phoneNumber);

    boolean notUniqueEmail(String email, String id);

    boolean notUniquePhoneNumber(String phoneNumber, String id);
}
