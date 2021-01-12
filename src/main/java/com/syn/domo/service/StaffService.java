package com.syn.domo.service;

import com.syn.domo.model.service.StaffServiceModel;
import com.syn.domo.model.view.ResponseModel;

import java.util.Optional;
import java.util.Set;

public interface StaffService {

    Set<StaffServiceModel> getAll(String buildingId);

    Optional<StaffServiceModel> get(String staffId);

    ResponseModel<StaffServiceModel> add(StaffServiceModel staffServiceModel);

    StaffServiceModel edit(StaffServiceModel staffServiceModel, String staffId);

    void deleteAll(String buildingId);

    void delete(String staffId);

    void assignBuildings(String staffId, Set<String> buildingIds);

    void cancelBuildingAssignments(Set<String> staffIds, String buildingId);

    Set<StaffServiceModel> getAllByIdIn(Set<String> staffIds);
}
