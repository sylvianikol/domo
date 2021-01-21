package com.syn.domo.service;

import com.syn.domo.model.service.ChildServiceModel;
import com.syn.domo.model.view.ResponseModel;
import com.syn.domo.web.filter.ChildFilter;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.Set;

public interface ChildService {

    Set<ChildServiceModel> getAll(ChildFilter childFilter, Pageable pageable);

    Optional<ChildServiceModel> get(String childId);

    ResponseModel<ChildServiceModel> add(ChildServiceModel childServiceModel,
                                         String buildingId, String apartmentId,
                                         Set<String> parentIds);

    ResponseModel<ChildServiceModel> edit(ChildServiceModel childServiceModel, String childId);

    int deleteAll(ChildFilter childFilter);

    void delete(String childId);

}
