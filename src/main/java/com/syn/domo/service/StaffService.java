package com.syn.domo.service;

import com.syn.domo.model.service.StaffServiceModel;
import com.syn.domo.model.service.UserServiceModel;

import java.util.Optional;
import java.util.Set;

public interface StaffService {

    StaffServiceModel add(UserServiceModel userServiceModel);

    StaffServiceModel edit(UserServiceModel userServiceModel);

    void delete(String staffId);

    Optional<StaffServiceModel> getOne(String staffId);

    Set<StaffServiceModel> getAll();
}
