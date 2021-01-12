package com.syn.domo.service;

import com.syn.domo.model.service.ChildServiceModel;
import com.syn.domo.model.view.ResponseModel;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

public interface ChildService {

    Set<ChildServiceModel> getAll(String buildingId, String apartmentId);

    Optional<ChildServiceModel> get(String buildingId, String apartmentId, String childId);

    ResponseModel<ChildServiceModel> add(ChildServiceModel childServiceModel, String buildingId, String apartmentId);

    ChildServiceModel edit(ChildServiceModel childServiceModel,
                           String buildingId, String apartmentId, String childId);

    void deleteAll(String buildingId, String apartmentId);

    void delete(String childId, String buildingId, String apartmentId);

}
