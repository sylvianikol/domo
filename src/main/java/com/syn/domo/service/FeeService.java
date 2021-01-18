package com.syn.domo.service;

import com.syn.domo.model.service.FeeServiceModel;
import com.syn.domo.web.filter.FeeFilter;
import org.springframework.data.domain.Pageable;

import javax.mail.MessagingException;
import java.util.Optional;
import java.util.Set;

public interface FeeService {

    Set<FeeServiceModel> getAll(FeeFilter feeFilter, Pageable pageable);

    Optional<FeeServiceModel> get(String feeId);

    FeeServiceModel pay(String userId, String feeId) throws MessagingException;

    void deleteAll(FeeFilter feeFilter);

    void delete(String feeId);

    void generateMonthlyFees() throws MessagingException;
}
