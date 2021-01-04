package com.syn.domo.service;

import com.syn.domo.model.service.ChildServiceModel;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface ChildService {

    ChildServiceModel add(ChildServiceModel childServiceModel, String buildingId, String apartmentId);

    ChildServiceModel edit(ChildServiceModel childServiceModel, String buildingId, String apartmentId);

    Optional<ChildServiceModel> getById(String childId);

    void delete(String childId, String buildingId, String apartmentId);

    Set<ChildServiceModel> getAllByApartmentIdAndBuildingId(String buildingId, String apartmentId);

    void deleteAllByApartmentId(String buildingId, String apartmentId);

    Optional<ChildServiceModel> getOne(String buildingId, String apartmentId, String childId);
}
