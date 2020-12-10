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

        resident.setRole(UserRole.USER);

        ApartmentServiceModel apartmentServiceModel =
                this.apartmentService.getByNumber(residentServiceModel.getApartmentNumber());

        resident.setApartment(this.modelMapper.map(apartmentServiceModel, Apartment.class));

        this.residentRepository.saveAndFlush(resident);

        return residentServiceModel;
    }
}
