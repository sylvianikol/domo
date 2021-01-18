package com.syn.domo.service;

import com.syn.domo.model.service.ResidentServiceModel;
import com.syn.domo.model.view.ResponseModel;

import javax.mail.MessagingException;
import org.springframework.data.domain.Pageable;
import java.util.Optional;
import java.util.Set;

public interface ResidentService {

    Set<ResidentServiceModel> getAll(String buildingId, String apartmentId, Pageable pageable);

    Optional<ResidentServiceModel> get(String residentId);

    ResponseModel<ResidentServiceModel> add(ResidentServiceModel residentServiceModel,
                                            String buildingId, String apartmentId) throws MessagingException;

    ResponseModel<ResidentServiceModel> edit(ResidentServiceModel residentServiceModel,
                                             String residentId);

    void deleteAll(String buildingId, String apartmentId);

    void delete(String residentId);

    Set<ResidentServiceModel> getAllByIdIn(Set<String> ids);

    Optional<ResidentServiceModel> getOneByIdAndBuildingIdAndApartmentId(String buildingId, String apartmentId,
                                                                         String residentId);

    Optional<ResidentServiceModel> getOneByIdAndBuildingId(String residentId, String buildingId);

    Optional<ResidentServiceModel> getOneByIdAndApartmentId(String residentId, String apartmentId);

}
