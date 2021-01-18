package com.syn.domo.service;

import com.syn.domo.model.service.FeeServiceModel;
import org.springframework.data.domain.Pageable;

import javax.mail.MessagingException;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface FeeService {

    Set<FeeServiceModel> getAll(String buildingId, String apartmentId, Pageable pageable);

    Optional<FeeServiceModel> get(String feeId);

    void delete(String feeId);

    FeeServiceModel pay(String userId, String feeId) throws MessagingException;

    void generateMonthlyFees() throws MessagingException;

    void deleteAll(String buildingId, String apartmentId);
}
