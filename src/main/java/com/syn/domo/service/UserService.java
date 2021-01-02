package com.syn.domo.service;

import com.syn.domo.model.service.UserServiceModel;

import java.util.Optional;
import java.util.Set;

public interface UserService {

    UserServiceModel add(UserServiceModel userServiceModel, String buildingId, String apartmentId);

    UserServiceModel edit(UserServiceModel userServiceModel, String buildingId, String apartmentId);

    Set<UserServiceModel> getAllByApartmentIdAndBuildingId(String buildingId, String apartmentId);

    Set<UserServiceModel> getAllById(Set<String> ids);

    Optional<UserServiceModel> getById(String residentId);

    void deleteAllByApartmentId(String buildingId, String apartmentId);

    void delete(String residentId, String buildingId, String apartmentId);

    void initAdmin();
}
