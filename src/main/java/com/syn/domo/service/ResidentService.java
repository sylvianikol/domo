package com.syn.domo.service;

import com.syn.domo.model.service.ResidentServiceModel;
import com.syn.domo.model.service.UserServiceModel;

public interface ResidentService {

    ResidentServiceModel add(UserServiceModel userServiceModel, String buildingId, String apartmentId);
}
