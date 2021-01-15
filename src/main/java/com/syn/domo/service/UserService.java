package com.syn.domo.service;

import com.syn.domo.model.service.UserServiceModel;
import com.syn.domo.model.view.ResponseModel;

import java.util.Optional;

public interface UserService {

    void initAdmin();

    Optional<UserServiceModel> get(String userId);

    ResponseModel<UserServiceModel> createPassword(String userId, String password, String confirmPassword);

    Optional<UserServiceModel> getByEmail(String email);

    Optional<UserServiceModel> getByPhoneNumber(String phoneNumber);

    boolean notUniqueEmail(String email, String id);

    boolean notUniquePhoneNumber(String phoneNumber, String id);
}
