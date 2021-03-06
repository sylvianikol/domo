package com.syn.domo.service;

import com.syn.domo.model.service.StaffServiceModel;
import com.syn.domo.web.filter.StaffFilter;
import org.springframework.data.domain.Pageable;

import javax.mail.MessagingException;
import java.util.Optional;
import java.util.Set;

public interface StaffService {

    Set<StaffServiceModel> getAll(StaffFilter staffFilter, Pageable pageable);

    Set<StaffServiceModel> getAll();

    Optional<StaffServiceModel> get(String staffId);

    StaffServiceModel add(StaffServiceModel staffServiceModel) throws MessagingException, InterruptedException;

    StaffServiceModel edit(StaffServiceModel staffServiceModel, String staffId);

    int deleteAll(StaffFilter staffFilter);

    void delete(String staffId);

    void assignBuildings(String staffId, Set<String> buildingIds);

    void cancelBuildingAssignments(Set<String> staffIds, String buildingId);

    Set<StaffServiceModel> getAllByIdIn(Set<String> staffIds);

    void paySalaries();
}
