package com.syn.domo.service;

import com.syn.domo.model.service.ChildServiceModel;

import java.util.Optional;
import java.util.Set;

public interface ChildService {

    Set<ChildServiceModel> getAllByApartmentId(String apartmentId);

    ChildServiceModel add(ChildServiceModel childServiceModel, String buildingId, String apartmentId);

    ChildServiceModel edit(ChildServiceModel childServiceModel, String buildingId, String apartmentId);

    Optional<ChildServiceModel> getById(String childId);

//    void removeAllByApartmentId(String apartmentId);
}
