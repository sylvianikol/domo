package com.syn.domo.service;

import com.syn.domo.model.binding.StaffAssignBuildingsBindingModel;
import com.syn.domo.model.service.StaffServiceModel;
import com.syn.domo.model.service.UserServiceModel;

import java.util.Optional;
import java.util.Set;

public interface StaffService {

    StaffServiceModel add(StaffServiceModel staffServiceModel);

    StaffServiceModel edit(StaffServiceModel staffServiceModel);

    void delete(String staffId);

    Optional<StaffServiceModel> getOne(String staffId);

    Set<StaffServiceModel> getAll();

    void assignBuildings(String staffId, Set<String> buildingIds);

    void releaseBuilding(Set<String> staffIds, String buildingId);

    Set<StaffServiceModel> getAllByIdIn(Set<String> staffIds);
}
