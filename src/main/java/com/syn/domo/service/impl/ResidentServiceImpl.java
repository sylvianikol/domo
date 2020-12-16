package com.syn.domo.service.impl;

import com.syn.domo.model.entity.Apartment;
import com.syn.domo.model.entity.Resident;
import com.syn.domo.model.entity.UserRole;
import com.syn.domo.model.service.ApartmentServiceModel;
import com.syn.domo.model.service.ResidentServiceModel;
import com.syn.domo.repository.ResidentRepository;
import com.syn.domo.service.ApartmentService;
import com.syn.domo.service.ResidentService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ResidentServiceImpl implements ResidentService {

    private final ResidentRepository residentRepository;
    private final ApartmentService apartmentService;
    private final ModelMapper modelMapper;

    @Autowired
    public ResidentServiceImpl(ResidentRepository residentRepository, ApartmentService apartmentService, ModelMapper modelMapper) {
        this.residentRepository = residentRepository;
        this.apartmentService = apartmentService;
        this.modelMapper = modelMapper;
    }


    @Override
    public ResidentServiceModel register(ResidentServiceModel residentServiceModel) {

        Resident resident = this.modelMapper.map(residentServiceModel, Resident.class);

        resident.setUserRole(UserRole.USER);

        ApartmentServiceModel apartmentServiceModel =
                this.apartmentService.getByNumberAndBuildingId(residentServiceModel.getApartmentNumber(), "buildingId");

        resident.setApartment(this.modelMapper.map(apartmentServiceModel, Apartment.class));

        this.residentRepository.saveAndFlush(resident);

        return residentServiceModel;
    }

    @Override
    public Set<ResidentServiceModel> getAllResidents() {
        Set<ResidentServiceModel> residentServiceModels =
                this.residentRepository.findAllByOrderByApartment_Number().stream()
                .map(resident -> this.modelMapper.map(resident, ResidentServiceModel.class))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        return Collections.unmodifiableSet(residentServiceModels);
    }
}
