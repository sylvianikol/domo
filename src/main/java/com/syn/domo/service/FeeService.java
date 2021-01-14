package com.syn.domo.service;

import com.syn.domo.model.service.FeeServiceModel;

import java.util.Map;
import java.util.Optional;

public interface FeeService {

    Map<String, Object> getAll(String buildingId, String apartmentId,
                               int page, int size, String[] sort);

    Optional<FeeServiceModel> get(String feeId);

    void delete(String feeId);

    FeeServiceModel pay(String feeId);

    void generateMonthlyFees();

    void deleteAll(String buildingId);

}
