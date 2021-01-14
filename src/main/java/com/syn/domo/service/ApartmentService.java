package com.syn.domo.service;

import com.syn.domo.model.service.ApartmentServiceModel;
import com.syn.domo.model.view.ResponseModel;

import java.util.Optional;
import java.util.Set;

public interface ApartmentService {

    Set<ApartmentServiceModel> getAll(String buildingId);

    Optional<ApartmentServiceModel> get(String apartmentId);

    ResponseModel<ApartmentServiceModel> add(ApartmentServiceModel addServiceModel, String buildingId);

    ResponseModel<ApartmentServiceModel> edit(ApartmentServiceModel apartmentServiceModel, String apartmentId);

    void deleteAll(String buildingId);

    void delete(String apartmentId);

    void emptyApartments(String building);

    Optional<ApartmentServiceModel> getByIdAndBuildingId(String apartmentId, String buildingId1);
}

