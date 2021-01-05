package com.syn.domo.service;

import com.syn.domo.model.service.StaffServiceModel;

public interface StaffService {

    StaffServiceModel add(StaffServiceModel staffServiceModel);

    StaffServiceModel edit(StaffServiceModel staffServiceModel);

    void delete(String staffId);
}
