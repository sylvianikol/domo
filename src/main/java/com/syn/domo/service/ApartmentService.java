package com.syn.domo.service;

import com.syn.domo.model.service.ApartmentServiceModel;

import com.syn.domo.web.filter.ApartmentFilter;
import org.springframework.data.domain.Pageable;
import java.util.Optional;
import java.util.Set;

public interface ApartmentService {

    Set<ApartmentServiceModel> getAll(ApartmentFilter apartmentFilter, Pageable pageable);

    Set<ApartmentServiceModel> getAll();

    Optional<ApartmentServiceModel> get(String apartmentId);

    ApartmentServiceModel add(ApartmentServiceModel addServiceModel, String buildingId);

    ApartmentServiceModel edit(ApartmentServiceModel apartmentServiceModel, String apartmentId);

    int deleteAll(ApartmentFilter apartmentFilter);

    void delete(String apartmentId);

    void evacuateApartments(String building);

    Optional<ApartmentServiceModel> getByIdAndBuildingId(String apartmentId, String buildingId1);
}

