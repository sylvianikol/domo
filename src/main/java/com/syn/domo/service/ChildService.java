package com.syn.domo.service;

import com.syn.domo.model.service.ChildServiceModel;

import java.util.Set;

public interface ChildService {

    Set<ChildServiceModel> getAllChildrenByApartmentId(String apartmentId);

    ChildServiceModel add(ChildServiceModel childServiceModel, String apartmentId);

    ChildServiceModel getById(String childId);
}
