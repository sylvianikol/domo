package com.syn.domo.service;

import com.syn.domo.model.service.StaffServiceModel;

import java.util.Optional;
import java.util.Set;

public interface StaffService {

    StaffServiceModel add(StaffServiceModel staffServiceModel);

    StaffServiceModel edit(StaffServiceModel staffServiceModel);

    void delete(String staffId);

    Optional<StaffServiceModel> getOne(String staffId);

    Set<StaffServiceModel> getAll();
}
