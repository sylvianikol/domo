package com.syn.domo.service;

import com.syn.domo.model.service.BuildingServiceModel;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;
import org.springframework.data.domain.Pageable;

public interface BuildingService {

    Set<BuildingServiceModel> getAll(Pageable pageable);

    Set<BuildingServiceModel> getAll();

    Optional<BuildingServiceModel> get(String id);

    BuildingServiceModel add(BuildingServiceModel buildingServiceModel);

    BuildingServiceModel edit(BuildingServiceModel buildingServiceModel, String buildingId);

    int deleteAll();

    void delete(String buildingId);

    void assignStaff(String buildingId, Set<String> staffIds);

    Set<BuildingServiceModel> getAllByIdIn(Set<String> ids);

    void addToBudget(BigDecimal total, String buildingId);

    void updateBudget(String buildingId, BigDecimal budget);
}
