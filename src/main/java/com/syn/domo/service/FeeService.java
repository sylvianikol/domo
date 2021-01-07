package com.syn.domo.service;

import com.syn.domo.model.service.FeeServiceModel;

import java.util.Map;
import java.util.Optional;

public interface FeeService {

    Map<String, Object> getAll(String buildingId, int page, int size, String[] sort);

    Optional<FeeServiceModel> getOne(String feeId, String buildingId);

    void generateMonthlyFees();

}
