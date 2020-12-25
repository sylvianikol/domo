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

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Optional;
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
    public ResidentServiceModel add(ResidentServiceModel residentServiceModel) {

        Resident resident = this.modelMapper.map(residentServiceModel, Resident.class);
        resident.setAddedOn(LocalDate.now());
        resident.setUserRole(UserRole.RESIDENT);

        Optional<ApartmentServiceModel> apartmentServiceModel = this.apartmentService.getById(residentServiceModel.getApartment().getId());

        resident.setApartment(this.modelMapper.map(apartmentServiceModel.get(), Apartment.class));

        this.residentRepository.saveAndFlush(resident);

        return this.modelMapper.map(resident, ResidentServiceModel.class);
    }

    @Override
    public Set<ResidentServiceModel> getAllResidentsByApartmentId(String apartmentId) {
        Set<ResidentServiceModel> residentServiceModels =
                this.residentRepository.findAllByApartment_Id(apartmentId)
                .stream()
                .map(resident -> this.modelMapper.map(resident, ResidentServiceModel.class))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        return Collections.unmodifiableSet(residentServiceModels);
    }

    @Override
    public ResidentServiceModel getById(String residentId) {

        Resident resident = this.residentRepository.findById(residentId)
                .orElseThrow(() -> {
                    throw new EntityNotFoundException("Resident not found");
                });

        return this.modelMapper.map(resident, ResidentServiceModel.class);
    }

    @Override
    public void archiveAllByApartmentId(String apartmentId) {
        this.residentRepository.findAllByApartment_Id(apartmentId)
                .forEach(resident -> {
                    resident.setRemovedOn(LocalDate.now());
                    this.residentRepository.saveAndFlush(resident);
                });
    }

    @Override
    @Transactional
    public void deleteAllByApartmentId(String apartmentId) {
        Set<Resident> residents = this.residentRepository.findAllByApartment_Id(apartmentId);
        this.residentRepository.deleteAll(residents);
    }
}
