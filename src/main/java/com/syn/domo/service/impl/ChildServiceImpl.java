package com.syn.domo.service.impl;

import com.syn.domo.model.entity.Apartment;
import com.syn.domo.model.entity.Child;
import com.syn.domo.model.service.ApartmentServiceModel;
import com.syn.domo.model.service.ChildServiceModel;
import com.syn.domo.repository.ChildRepository;
import com.syn.domo.service.ApartmentService;
import com.syn.domo.service.ChildService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ChildServiceImpl implements ChildService {

    private final ChildRepository childRepository;
    private final ApartmentService apartmentService;
    private final ModelMapper modelMapper;

    @Autowired
    public ChildServiceImpl(ChildRepository childRepository, ApartmentService apartmentService, ModelMapper modelMapper) {
        this.childRepository = childRepository;
        this.apartmentService = apartmentService;
        this.modelMapper = modelMapper;
    }

    @Override
    public Set<ChildServiceModel> getAllChildrenByApartmentId(String apartmentId) {
        Set<ChildServiceModel> childServiceModels = this.childRepository.findAllByApartment_Id(apartmentId).stream()
                .map(child -> this.modelMapper.map(child, ChildServiceModel.class))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        return Collections.unmodifiableSet(childServiceModels);
    }

    @Override
    public ChildServiceModel add(ChildServiceModel childServiceModel) {
        // TODO: validation
        Child child = this.modelMapper.map(childServiceModel, Child.class);
        child.setAddedOn(LocalDate.now());

        Optional<ApartmentServiceModel> apartmentServiceModel =
                this.apartmentService.getById(childServiceModel.getApartment().getId());

        child.setApartment(this.modelMapper.map(apartmentServiceModel.get(), Apartment.class));

        this.childRepository.saveAndFlush(child);

        return this.modelMapper.map(child, ChildServiceModel.class);
    }

    @Override
    public ChildServiceModel getById(String childId) {
        // TODO: ChildNotFoundException
        return this.childRepository.findById(childId)
                .map(child -> this.modelMapper.map(child, ChildServiceModel.class))
                .orElseThrow(() -> {
                    throw new EntityNotFoundException("Child not found!");
                });
    }

    @Override
    public void removeAllByApartmentId(String apartmentId) {
        this.childRepository.findAllByApartment_Id(apartmentId)
                .forEach(child -> {
                    child.setRemovedOn(LocalDate.now());
                    this.childRepository.saveAndFlush(child);
                });
    }
}
