package com.syn.domo.service;

import com.syn.domo.model.service.ApartmentServiceModel;

import java.util.Optional;
import java.util.Set;

public interface ApartmentService {

    ApartmentServiceModel add(ApartmentServiceModel addServiceModel, String buildingId);

    void deleteAllByBuildingId(String buildingId);

    Set<ApartmentServiceModel> getAllApartmentsByBuildingId(String buildingId);

    Set<String> getAllApartmentNumbersByBuildingId(String buildingId);

    boolean alreadyExists(String apartmentNumber, String buildingId);

    ApartmentServiceModel getByNumberAndBuildingId(String apartmentNumber, String buildingId);

    ApartmentServiceModel getById(String apartmentId);

    Optional<ApartmentServiceModel> getOptById(String apartmentId);

    boolean hasResidents(String apartmentId);

    void archiveAllByBuildingId(String buildingId);
}
