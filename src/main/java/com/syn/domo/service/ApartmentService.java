package com.syn.domo.service;

import com.syn.domo.model.entity.Apartment;
import com.syn.domo.model.service.ApartmentServiceModel;

import java.util.Set;

public interface ApartmentService {

    ApartmentServiceModel add(ApartmentServiceModel addServiceModel);

    Set<ApartmentServiceModel> getAllApartments();

    ApartmentServiceModel getByNumber(String apartmentNumber);
}
