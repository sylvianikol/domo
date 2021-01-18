package com.syn.domo.service;

import com.syn.domo.model.service.ResidentServiceModel;
import com.syn.domo.model.view.ResponseModel;

import javax.mail.MessagingException;

import com.syn.domo.web.filter.ResidentFilter;
import org.springframework.data.domain.Pageable;
import java.util.Optional;
import java.util.Set;

public interface ResidentService {

    Set<ResidentServiceModel> getAll(ResidentFilter residentFilter, Pageable pageable);

    Optional<ResidentServiceModel> get(String residentId);

    ResponseModel<ResidentServiceModel> add(ResidentServiceModel residentServiceModel,
                                            String buildingId, String apartmentId) throws MessagingException;

    ResponseModel<ResidentServiceModel> edit(ResidentServiceModel residentServiceModel,
                                             String residentId);

    void deleteAll(ResidentFilter residentFilter);

    void delete(String residentId);

    Set<ResidentServiceModel> getAllByIdIn(Set<String> ids);
}
