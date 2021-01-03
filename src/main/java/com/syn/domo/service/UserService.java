package com.syn.domo.service;

import com.syn.domo.model.entity.Role;
import com.syn.domo.model.entity.UserRole;
import com.syn.domo.model.service.RoleServiceModel;
import com.syn.domo.model.service.UserServiceModel;

import java.util.Optional;
import java.util.Set;

public interface UserService {

    UserServiceModel add(UserServiceModel userServiceModel, Set<RoleServiceModel> roleServiceModels);

    UserServiceModel edit(UserServiceModel userServiceModel, String buildingId, String apartmentId);

    Set<UserServiceModel> getAllByApartmentIdAndBuildingId(String buildingId, String apartmentId);

    Set<UserServiceModel> getAllById(Set<String> ids);

    Optional<UserServiceModel> getById(String residentId);

    void deleteAllByApartmentId(String buildingId, String apartmentId);

    void delete(String residentId, String buildingId, String apartmentId);

    void initAdmin();

    Optional<UserServiceModel> getByEmail(String email);

    boolean notUniqueEmail(String email, String id);
}
