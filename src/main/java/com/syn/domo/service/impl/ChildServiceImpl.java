package com.syn.domo.service.impl;

import com.syn.domo.model.service.ChildServiceModel;
import com.syn.domo.repository.ChildRepository;
import com.syn.domo.service.ChildService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ChildServiceImpl implements ChildService {

    private final ChildRepository childRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ChildServiceImpl(ChildRepository childRepository, ModelMapper modelMapper) {
        this.childRepository = childRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Set<ChildServiceModel> getAllChildrenByApartmentId(String apartmentId) {
        Set<ChildServiceModel> childServiceModels = this.childRepository.findAllByApartment_Id(apartmentId).stream()
                .map(child -> this.modelMapper.map(child, ChildServiceModel.class))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        return Collections.unmodifiableSet(childServiceModels);
    }
}
