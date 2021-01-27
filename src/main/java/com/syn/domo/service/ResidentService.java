package com.syn.domo.service;

import com.syn.domo.model.service.ResidentServiceModel;

import javax.mail.MessagingException;

import com.syn.domo.web.filter.ResidentFilter;
import org.springframework.data.domain.Pageable;
import java.util.Optional;
import java.util.Set;

public interface ResidentService {

    Set<ResidentServiceModel> getAll(ResidentFilter residentFilter, Pageable pageable);

    Optional<ResidentServiceModel> get(String residentId);

    ResidentServiceModel add(ResidentServiceModel residentServiceModel,
                                            String buildingId, String apartmentId) throws MessagingException, InterruptedException;

    ResidentServiceModel edit(ResidentServiceModel residentServiceModel,
                                             String residentId);

    int deleteAll(ResidentFilter residentFilter);

    void delete(String residentId);

    Set<ResidentServiceModel> getAllByIdIn(Set<String> ids);
}
