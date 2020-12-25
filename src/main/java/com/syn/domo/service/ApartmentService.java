package com.syn.domo.service;

import com.syn.domo.model.service.ApartmentServiceModel;

import java.util.Optional;
import java.util.Set;

public interface ApartmentService {

    ApartmentServiceModel add(ApartmentServiceModel addServiceModel, String buildingId);

    ApartmentServiceModel edit(ApartmentServiceModel apartmentServiceModel, String buildingId);

    void deleteAllByBuildingId(String buildingId);

    Set<ApartmentServiceModel> getAllApartmentsByBuildingId(String buildingId);

    ApartmentServiceModel getByNumberAndBuildingId(String apartmentNumber, String buildingId);

    Optional<ApartmentServiceModel> getById(String apartmentId);
}
