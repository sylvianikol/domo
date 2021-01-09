package com.syn.domo.service;

import com.syn.domo.model.service.ApartmentServiceModel;

import java.util.Optional;
import java.util.Set;

public interface ApartmentService {

    ApartmentServiceModel add(ApartmentServiceModel addServiceModel, String buildingId);

    ApartmentServiceModel edit(ApartmentServiceModel apartmentServiceModel, String buildingId);

    void delete(String apartmentId, String buildingId);

    void deleteAllByBuildingId(String buildingId);

    Set<ApartmentServiceModel> getAllByBuildingId(String buildingId);

    Optional<ApartmentServiceModel> getById(String apartmentId);

    Optional<ApartmentServiceModel> getByIdIn(String id, Set<String> apartmentIds);

    void emptyApartments(String building);
}

