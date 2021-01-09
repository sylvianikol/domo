package com.syn.domo.service;

import com.syn.domo.model.service.BuildingServiceModel;

import java.util.Optional;
import java.util.Set;

public interface BuildingService {

    Set<BuildingServiceModel> getAll();

    Optional<BuildingServiceModel> get(String id);

    BuildingServiceModel add(BuildingServiceModel buildingServiceModel);

    BuildingServiceModel edit(BuildingServiceModel buildingServiceModel, String buildingId);

    void deleteAll();

    void delete(String buildingId);

    void assignStaff(String buildingId, Set<String> staffIds);

    Set<BuildingServiceModel> getAllByIdIn(Set<String> ids);
}
