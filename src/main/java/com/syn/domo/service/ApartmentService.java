package com.syn.domo.service;

import com.syn.domo.model.service.ApartmentServiceModel;

import java.util.Optional;
import java.util.Set;

public interface ApartmentService {

    ApartmentServiceModel add(ApartmentServiceModel addServiceModel, String buildingId);

    ApartmentServiceModel edit(ApartmentServiceModel apartmentServiceModel,
                               String buildingId, String apartmentId);

    void delete(String apartmentId, String buildingId);

    void deleteAll(String buildingId);

    Set<ApartmentServiceModel> getAll(String buildingId);

    Optional<ApartmentServiceModel> get(String apartmentId);

    Optional<ApartmentServiceModel> getByIdIn(String id, Set<String> apartmentIds);

    void emptyApartments(String building);
}

